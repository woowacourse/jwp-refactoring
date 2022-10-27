package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixture.두명인_테이블;
import static kitchenpos.fixture.OrderTableFixture.주문가능_테이블;
import static kitchenpos.fixture.OrderTableFixture.테이블그룹이_존재하지_않는_테이블;
import static kitchenpos.fixture.OrderTableFixture.한명인_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
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
        final OrderTable orderTable = OrderTableFixture.createOrderTable(actualNumberOfGuests, true);

        final OrderTable actual = tableService.create(orderTable);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(actualNumberOfGuests),
                () -> assertThat(actual.getTableGroupId()).isNull(),
                () -> assertThat(actual.isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("테이블 목록들을 조회한다.")
    void getTables() {
        final List<OrderTable> expectedOrderTables = orderTableDao.findAll();

        final List<OrderTable> orderTables = tableService.list();

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

        final OrderTable emptyTable = OrderTableFixture.createEmptyTable();

        final OrderTable changedOrderTable = tableService.changeEmpty(orderTable.getId(), emptyTable);

        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블이 기존 테이블 그룹에 포함되어 있는 경우 예외 발생")
    void whenOrderTableIsIncludeInTableGroup() {
        final OrderTable emptyTable = OrderTableFixture.createEmptyTable();

        assertThatThrownBy(() -> tableService.changeEmpty(주문가능_테이블, emptyTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 주문의 상태가 조리 상태면 예외 발생")
    void whenOrderTableWithCookingStatus() {
        final OrderTable savedOrderTableId = orderTableDao.save(OrderTableFixture.createOrderTableByEmpty(false));
        orderDao.save(OrderFixture.createOrder(savedOrderTableId.getId(), OrderStatus.COOKING.name()));
        final OrderTable emptyTable = OrderTableFixture.createEmptyTable();

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTableId.getId(), emptyTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 주문의 상태가 식사 상태면 예외 발생")
    void whenOrderTableWithMealStatus() {
        final OrderTable savedOrderTableId = orderTableDao.save(OrderTableFixture.createOrderTableByEmpty(false));
        orderDao.save(OrderFixture.createOrder(savedOrderTableId.getId(), OrderStatus.MEAL.name()));
        final OrderTable emptyTable = OrderTableFixture.createEmptyTable();

        assertThatThrownBy(() -> tableService.changeEmpty(두명인_테이블, emptyTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    void changeNumberOrGuests() {
        final OrderTable orderTable = OrderTableFixture.createOrderTable(3, false);
        final OrderTable savedOrderTable = tableService.create(orderTable);

        final OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(),
                OrderTableFixture.createOrderTable(5));

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    @DisplayName("손님의 수가 음수이면 예외 발생")
    void whenNumberOfGuestsIsNegative() {
        final OrderTable orderTable = OrderTableFixture.createOrderTable(3, false);
        final OrderTable savedOrderTable = tableService.create(orderTable);

        final OrderTable changedOrderTable = OrderTableFixture.createOrderTable(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), changedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("기존의 주문 테이블이 존재하지 않을 경우 예외 발생")
    void whenInvalidOrderTableId() {
        final OrderTable changedOrderTable = OrderTableFixture.createOrderTable(5, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(99999L, changedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("기존의 주문 테이블이 존재하지 않을 경우 예외 발생")
    void whenOrderTableIsEmpty() {
        final OrderTable savedOrderTable = orderTableDao.save(OrderTableFixture.createOrderTableByEmpty(true));
        final OrderTable changedOrderTable = OrderTableFixture.createOrderTable(5, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), changedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
