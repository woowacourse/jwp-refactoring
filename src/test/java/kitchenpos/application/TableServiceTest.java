package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.inmemorydao.InMemoryOrderDao;
import kitchenpos.inmemorydao.InMemoryOrderTableDao;

@DisplayName("TableService 테스트")
class TableServiceTest {
    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private TableService tableService;

    @BeforeEach
    void setUp() {
        this.orderDao = new InMemoryOrderDao();
        this.orderTableDao = new InMemoryOrderTableDao();
        this.tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("주문 테이블을 등록한다")
    @Test
    void create() {
        // Given
        final OrderTable orderTable = new OrderTable();

        // When
        final OrderTable savedOrderTable = tableService.create(orderTable);

        // Then
        assertThat(savedOrderTable)
                .extracting(OrderTable::getId)
                .isNotNull()
        ;
    }

    @DisplayName("주문 테이블의 목록을 조회한다")
    @Test
    void list() {
        // Given
        final OrderTable orderTable = new OrderTable();
        orderTableDao.save(orderTable);

        // When
        final List<OrderTable> list = tableService.list();

        // Then
        assertThat(list).isNotEmpty();
    }

    @DisplayName("주문 테이블을 빈 테이블로 설정한다")
    @Test
    void changeEmpty() {
        // Given
        final OrderTable orderTable = new OrderTable();
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final OrderTable emptyOrderTable = new OrderTable();
        emptyOrderTable.setEmpty(true);

        // When
        final OrderTable changedEmptyOrderTable = tableService.changeEmpty(savedOrderTable.getId(),
                emptyOrderTable);

        // Then
        assertAll(
                () -> assertThat(changedEmptyOrderTable)
                        .extracting(OrderTable::getId)
                        .isEqualTo(savedOrderTable.getId())
                ,
                () -> assertThat(changedEmptyOrderTable)
                        .extracting(OrderTable::isEmpty)
                        .isEqualTo(true)
        );
    }

    @DisplayName("주문 테이블이 존재하지 않을 경우 예외가 발생한다")
    @Test
    void changeEmpty_OrderTableIdNotExists_ExceptionThrown() {
        // Given
        final OrderTable emptyOrderTable = new OrderTable();
        emptyOrderTable.setEmpty(true);

        // Then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, emptyOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @DisplayName("주문 테이블이 단체 지정이 되어있을 경우 예외가 발생한다")
    @Test
    void changeEmpty_TableGroupIdExists_ExceptionThrown() {
        // Given
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final OrderTable emptyOrderTable = new OrderTable();
        emptyOrderTable.setEmpty(true);

        // Then
        final Long orderTableId = savedOrderTable.getId();

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, emptyOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @DisplayName("주문 테이블의 주문 상태가 조리 중 또는 식사 중인 경우 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmpty_OrderStatusIsCookingOrMeal_ExceptionThrown(final String orderStatus) {
        // Given
        final OrderTable orderTable = new OrderTable();
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderStatus(orderStatus);
        order.setOrderTableId(savedOrderTable.getId());
        orderDao.save(order);

        final OrderTable emptyOrderTable = new OrderTable();
        emptyOrderTable.setEmpty(true);

        // Then
        final Long orderTableId = savedOrderTable.getId();

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, emptyOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 설정한다")
    @Test
    void changeNumberOfGuests() {
        // Given
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final OrderTable changingOrderTable = new OrderTable();
        changingOrderTable.setNumberOfGuests(4);

        // When
        final OrderTable changedOrderTable = tableService.changeNumberOfGuests(
                savedOrderTable.getId(),
                changingOrderTable);

        // Then
        assertAll(
                () -> assertThat(changedOrderTable)
                        .extracting(OrderTable::getId)
                        .isEqualTo(savedOrderTable.getId())
                ,
                () -> assertThat(changedOrderTable)
                        .extracting(OrderTable::getNumberOfGuests)
                        .isEqualTo(changingOrderTable.getNumberOfGuests())
        );
    }

    @DisplayName("설정할 방문한 손님 수가 0보다 작을 경우 예외가 발생한다")
    @Test
    void changeNumberOfGuests_NumberOfGuestsIsLowerThanZero_ExceptionThrown() {
        // Given
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final OrderTable changingOrderTable = new OrderTable();
        changingOrderTable.setNumberOfGuests(-1);

        // Then
        final Long orderTableId = savedOrderTable.getId();

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTableId, changingOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @DisplayName("주문 테이블이 존재하지 않을 경우 예외가 발생한다")
    @Test
    void changeNumberOfGuests_OrderTableNotExists_ExceptionThrown() {
        // Given
        final OrderTable changingOrderTable = new OrderTable();
        changingOrderTable.setNumberOfGuests(4);

        // Then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, changingOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @DisplayName("주문 테이블이 빈 테이블일 경우 예외가 발생한다")
    @Test
    void changeNumberOfGuests_OrderTableIsEmpty_ExceptionThrown() {
        // Given
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final OrderTable changingOrderTable = new OrderTable();
        changingOrderTable.setNumberOfGuests(4);

        // Then
        final Long orderTableId = savedOrderTable.getId();

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTableId, changingOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }
}
