package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = AutowireMode.ALL)
public class TableServiceTest {

    private final TableService tableService;
    private final TableGroupDao tableGroupDao;
    private final OrderTableDao orderTableDao;
    private final OrderDao orderDao;

    TableGroup tableGroup;
    List<OrderTable> orderTables;

    public TableServiceTest(TableService tableService, TableGroupDao tableGroupDao, OrderTableDao orderTableDao,
                            OrderDao orderDao) {
        this.tableService = tableService;
        this.tableGroupDao = tableGroupDao;
        this.orderTableDao = orderTableDao;
        this.orderDao = orderDao;
    }

    @BeforeEach
    void setUp() {
        orderTables = new ArrayList<>();
        orderTables.add(orderTableDao.save(new OrderTable(100, true)));
        orderTables.add(orderTableDao.save(new OrderTable(100, true)));

        tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), orderTables));
    }

    @DisplayName("존재하지 않는 주문 테이블의 경우 빈 테이블로 바꿀 수 없다.")
    @Test
    void changeEmptyWithNotSavedOrderTable() {
        OrderTable orderTable = orderTables.get(0);

        assertThatThrownBy(() -> tableService.changeEmpty(-1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정된 경우, 빈 테이블로 바꿀 수 없다.")
    @Test
    void changeEmptyWithGroupTable() {
        OrderTable orderTable = new OrderTable(100, true);
        orderTable.setTableGroupId(tableGroup.getId());
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("조리중이거나 식사중인 경우 빈 테이블로 바꿀 수 없다.")
    @Test
    void changeEmptyWithNotCookingOrMealState() {
        Order order = new Order();
        OrderTable orderTable = orderTables.get(0);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        Order savedOrder = orderDao.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("완료된 경우 EMPTY로 상태가 변경된다.")
    @Test
    void changeEmpty() {
        Order order = new Order();
        OrderTable orderTable = orderTables.get(0);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        orderDao.save(order);
        OrderTable emptyOrderTable = tableService.changeEmpty(orderTable.getId(), orderTable);

        assertThat(emptyOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("인원수가 음수인 경우 인원수 변경 시 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWithNegative() {
        OrderTable orderTable = new OrderTable(-1, true);
        Long orderTableId = orderTables.get(0).getId();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블인 경우 인원수 변경 시 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWithEmptyTable() {
        OrderTable orderTable = new OrderTable(10, true);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        Long orderTableId = savedOrderTable.getId();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("인원수가 성공적으로 변경된다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable(10, false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        OrderTable orderTableForChange = new OrderTable(100, false);
        Long orderTableId = savedOrderTable.getId();
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(orderTableId, orderTableForChange);

        assertThat(changedOrderTable.getNumberOfGuests())
                .isEqualTo(100);
    }

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable = new OrderTable(10, false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @DisplayName("모든 주문 테이블을 조회할 수 있다.")
    @Test
    void list() {
        assertThat(tableService.list()).hasSize(2);
    }
}
