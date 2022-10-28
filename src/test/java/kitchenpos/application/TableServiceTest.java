package kitchenpos.application;

import static kitchenpos.fixture.OrderFixture.generateOrder;
import static kitchenpos.fixture.OrderTableFixture.generateOrderTableCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.FakeOrderDao;
import kitchenpos.dao.FakeOrderTableDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableServiceTest {

    private TableService tableService;

    private OrderDao orderDao;
    private OrderTableDao orderTableDao;

    @BeforeEach
    void beforeEach() {
        this.orderDao = new FakeOrderDao();
        this.orderTableDao = new FakeOrderTableDao();
        this.tableService = new TableService(orderDao, orderTableDao);
    }

    @Test
    @DisplayName("주문 테이블을 생성한다.")
    void create() {
        // given
        OrderTableCreateRequest orderTableCreateRequest = generateOrderTableCreateRequest(0, true);

        // when
        OrderTable newOrderTable = tableService.create(orderTableCreateRequest);

        // then
        assertAll(
                () -> assertThat(newOrderTable.getId()).isNotNull(),
                () -> assertThat(newOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(newOrderTable.getNumberOfGuests()).isEqualTo(0),
                () -> assertThat(newOrderTable.isEmpty()).isTrue()
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
        List<OrderTable> products = tableService.list();

        // then
        assertThat(products.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("주문 테이블 상태를 변경한다.")
    void changeEmpty() {
        // given
        OrderTableCreateRequest orderTableCreateRequest = generateOrderTableCreateRequest(0, true);
        OrderTable orderTable = tableService.create(orderTableCreateRequest);

        // when
        OrderTable changeOrderTable = tableService.changeEmpty(orderTable.getId(), false);

        // then
        assertThat(changeOrderTable.isEmpty()).isEqualTo(false);
    }

    // TODO: 테이블 그룹이 등록 되어있을 때 변경 불가 테스트 추가
    @Test
    @DisplayName("주문 테이블 상태를 변경 시 주문 상태가 cooking이나 meal이면 예외를 반환한다.")
    void changeEmpty_WhenOrderStatusCooking() {
        // given
        OrderTableCreateRequest orderTableCreateRequest = generateOrderTableCreateRequest(0, true);
        OrderTable orderTable = tableService.create(orderTableCreateRequest);

        Order order = generateOrder(LocalDateTime.now(), orderTable.getId(), OrderStatus.COOKING.name());
        orderDao.save(order);
        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 Cooking이나 Meal일 경우 테이블의 상태를 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        // given
        OrderTableCreateRequest orderTableCreateRequest = generateOrderTableCreateRequest(0, false);
        OrderTable orderTable = tableService.create(orderTableCreateRequest);

        // when
        OrderTable changeOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), 8);

        // then
        assertThat(changeOrderTable.isEmpty()).isEqualTo(false);
        assertThat(changeOrderTable.getNumberOfGuests()).isEqualTo(8);
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경 시 손님의 수가 0보다 작다면 예외를 반환한다.")
    void changeNumberOfGuests_WhenInvalidNumberOfGuests() {
        // given
        OrderTableCreateRequest orderTableCreateRequest = generateOrderTableCreateRequest(0, false);
        OrderTable orderTable = tableService.create(orderTableCreateRequest);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님의 수는 0보다 작을 수 없습니다.");
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경 시 주문이 불가한 주문 테이블이라면 예외를 반환한다.")
    void changeNumberOfGuests_WhenEmptyTable() {
        // given
        OrderTableCreateRequest orderTableCreateRequest = generateOrderTableCreateRequest(0, true);
        OrderTable orderTable = tableService.create(orderTableCreateRequest);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), 8))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문을 등록할 수 없는 주문 테이블입니다.");
    }
}
