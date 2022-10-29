package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixture.두명인_테이블;
import static kitchenpos.fixture.OrderTableFixture.주문가능_테이블;
import static kitchenpos.fixture.OrderTableFixture.테이블그룹이_존재하지_않는_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.dto.request.OrderTableCreateReqeust;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableServiceTest {

    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private TableService tableService;

    @BeforeEach
    void setUp() {
        orderDao = OrderFixture.setUp().getOrderDao();
        orderTableDao = OrderTableFixture.setUp().getOrderTableDao();
        tableService = new TableService(orderDao, orderTableDao);
    }

    @Test
    @DisplayName("테이블을 생성한다.")
    void createTable() {
        final int actualNumberOfGuests = 2;
        final OrderTableCreateReqeust request = OrderTableFixture.createOrderTable(actualNumberOfGuests, true);

        final OrderTableResponse actual = tableService.create(request);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(actualNumberOfGuests),
                () -> assertThat(actual.isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("테이블 목록들을 조회한다.")
    void getTables() {
        final List<OrderTableResponse> expectedOrderTables = OrderTableFixture.setUp().getFixtures();

        final List<OrderTableResponse> orderTables = tableService.list();

        assertAll(
                () -> assertThat(orderTables).hasSameSizeAs(expectedOrderTables),
                () -> assertThat(orderTables)
                        .usingRecursiveComparison()
                        .isEqualTo(expectedOrderTables)
        );
    }

    @Test
    @DisplayName("테이블의 상태를 변경한다.")
    void changeEmptyTable() {
        final OrderTable orderTable = orderTableDao.findById(테이블그룹이_존재하지_않는_테이블)
                .orElseThrow();

        final OrderTableResponse changedOrderTable = tableService.changeEmpty(orderTable.getId(), true);

        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블의 주문의 상태가 조리 상태면 예외 발생")
    void whenOrderTableWithCookingStatus() {
        final OrderTable savedOrderTableId = orderTableDao.save(new OrderTable(null, 3, false));
        orderDao.save(OrderFixture.createOrder(savedOrderTableId.getId(), OrderStatus.COOKING.name()));

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTableId.getId(), true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 주문의 상태가 식사 상태면 예외 발생")
    void whenOrderTableWithMealStatus() {
        final OrderTable savedOrderTableId = orderTableDao.save(new OrderTable(null, 3, false));
        orderDao.save(OrderFixture.createOrder(savedOrderTableId.getId(), OrderStatus.MEAL.name()));

        assertThatThrownBy(() -> tableService.changeEmpty(두명인_테이블, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    void changeNumberOrGuests() {
        final OrderTableResponse changedOrderTable = tableService.changeNumberOfGuests(주문가능_테이블, 5);

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(5);
    }
}
