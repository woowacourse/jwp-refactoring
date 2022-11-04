package kitchenpos.application;

import static kitchenpos.fixture.OrderFixture.generateOrder;
import static kitchenpos.fixture.OrderTableFixture.generateOrderTableCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.FakeOrderDao;
import kitchenpos.dao.FakeOrderTableDao;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.TableValidator;
import kitchenpos.table.dto.request.OrderTableCreateRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableServiceTest {

    private TableService tableService;

    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private TableValidator tableValidator;

    @BeforeEach
    void beforeEach() {
        this.orderDao = new FakeOrderDao();
        this.orderTableDao = new FakeOrderTableDao();
        this.tableValidator = new TableValidator(orderDao);
        this.tableService = new TableService(orderTableDao, tableValidator);
    }

    @Test
    @DisplayName("주문 테이블을 생성한다.")
    void create() {
        // given
        OrderTableCreateRequest orderTableCreateRequest = generateOrderTableCreateRequest(0, true);

        // when
        OrderTableResponse orderTableResponse = tableService.create(orderTableCreateRequest);

        // then
        assertAll(
                () -> assertThat(orderTableResponse.getId()).isNotNull(),
                () -> assertThat(orderTableResponse.getTableGroupId()).isNull(),
                () -> assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(0),
                () -> assertThat(orderTableResponse.isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회한다.")
    void list() {
        // given
        OrderTableCreateRequest orderTableCreateRequest1 = generateOrderTableCreateRequest(0, true);
        tableService.create(orderTableCreateRequest1);
        OrderTableCreateRequest orderTableCreateRequest2 = generateOrderTableCreateRequest(0, true);
        tableService.create(orderTableCreateRequest2);

        // when
        List<OrderTableResponse> orderTableResponses = tableService.list();

        // then
        assertThat(orderTableResponses.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("주문 테이블 상태를 변경한다.")
    void changeEmpty() {
        // given
        OrderTableCreateRequest orderTableCreateRequest = generateOrderTableCreateRequest(0, true);
        OrderTableResponse orderTableResponse = tableService.create(orderTableCreateRequest);

        // when
        OrderTableResponse changeOrderTable = tableService.changeEmpty(orderTableResponse.getId(), false);

        // then
        assertThat(changeOrderTable.isEmpty()).isEqualTo(false);
    }

    @Test
    @DisplayName("주문 테이블 상태를 변경 시 주문 상태가 cooking이나 meal이면 예외를 반환한다.")
    void changeEmpty_WhenOrderStatusCooking() {
        // given
        OrderTableCreateRequest orderTableCreateRequest = generateOrderTableCreateRequest(0, false);
        OrderTableResponse orderTableResponse = tableService.create(orderTableCreateRequest);

        Order order = generateOrder(LocalDateTime.now(), orderTableResponse.getId(), OrderStatus.COOKING.name());
        Order savedOrder = orderDao.save(order);
        System.out.println(savedOrder.getOrderTableId());
        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrder.getOrderTableId(), true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 Cooking이나 Meal일 경우 테이블의 상태를 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        // given
        OrderTableCreateRequest orderTableCreateRequest = generateOrderTableCreateRequest(0, false);
        OrderTableResponse orderTableResponse = tableService.create(orderTableCreateRequest);

        // when
        OrderTableResponse changeOrderTable = tableService.changeNumberOfGuests(orderTableResponse.getId(), 8);

        // then
        assertThat(changeOrderTable.isEmpty()).isEqualTo(false);
        assertThat(changeOrderTable.getNumberOfGuests()).isEqualTo(8);
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경 시 손님의 수가 0보다 작다면 예외를 반환한다.")
    void changeNumberOfGuests_WhenInvalidNumberOfGuests() {
        // given
        OrderTableCreateRequest orderTableCreateRequest = generateOrderTableCreateRequest(0, false);
        OrderTableResponse orderTableResponse = tableService.create(orderTableCreateRequest);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableResponse.getId(), -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님의 수는 0보다 작을 수 없습니다.");
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경 시 주문이 불가한 주문 테이블이라면 예외를 반환한다.")
    void changeNumberOfGuests_WhenEmptyTable() {
        // given
        OrderTableCreateRequest orderTableCreateRequest = generateOrderTableCreateRequest(0, true);
        OrderTableResponse orderTableResponse = tableService.create(orderTableCreateRequest);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableResponse.getId(), 8))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문을 등록할 수 없는 주문 테이블입니다.");
    }
}
