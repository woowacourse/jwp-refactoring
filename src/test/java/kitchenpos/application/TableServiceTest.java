package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@DisplayName("Table 서비스 테스트")
@SpringBootTest
@Transactional
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    private TableGroup savedTableGroup;

    @BeforeEach
    void setUp() {
        // table group
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        savedTableGroup = tableGroupDao.save(tableGroup);
    }

    @DisplayName("테이블을 등록한다")
    @Test
    void create() {
        final OrderTable orderTable = new OrderTable();

        final OrderTable savedOrderTable = tableService.create(orderTable);

        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @DisplayName("테이블의 목록을 조회한다")
    @Test
    void list() {
        final List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(8);
    }

    @DisplayName("테이블을 비운다")
    @Test
    void changeEmpty() {
        final OrderTable orderTable = new OrderTable();
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        final OrderTable newOrderTable = new OrderTable();
        newOrderTable.setEmpty(true);
        final OrderTable changedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), newOrderTable);

        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블을 비울 때 주문 테이블이 존재해야 한다")
    @Test
    void changeEmptyOrderTableIsNotExist() {
        final long notSavedOrderTableId = 0L;

        assertThatThrownBy(() -> tableService.changeEmpty(notSavedOrderTableId, new OrderTable()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 비울 때 저장되어 있는 주문 테이블의 아이디가 null 이어야 한다")
    @Test
    void changeEmptyTableGroupIdIsNull() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(savedTableGroup.getId());
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), new OrderTable()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 비울 때 테이블의 주문 상태가 요리중이거나 식사중일 경우 테이블을 비울 수 없다")
    @Test
    void changeEmptyOrderStatusIsCompletion() {
        final OrderTable orderTable = new OrderTable();
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), new OrderTable()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님의 수를 변경한다")
    @Test
    void changeNumberOfGuests() {
        final OrderTable orderTable = new OrderTable();
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(1);

        final OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable);

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(1);
    }

    @DisplayName("테이블 손님의 수 변경 시 변경하려는 손님의 수는 0보다 커야한다")
    @Test
    void changeNumberOfGuestsNumberIsLowerZero() {
        final OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(0);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, newOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 손님의 수 변경 시 주문 테이블이 존재해야 한다")
    @Test
    void changeNumberOfGuestsOrderTableIsNotExist() {
        final OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(1);

        long notSavedOrderTable = 0L;
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(notSavedOrderTable, newOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 손님의 수 변경 시 테이블이 비어있으면 안된다")
    @Test
    void changeNumberOfGuestsOrderTableIsEmpty() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
