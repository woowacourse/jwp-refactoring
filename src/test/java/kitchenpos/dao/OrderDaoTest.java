package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderDaoTest extends DaoTest {

    private OrderDao orderDao;
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @BeforeEach
    void setUp() {
        orderDao = new JdbcTemplateOrderDao(dataSource);
        orderTableDao = new JdbcTemplateOrderTableDao(dataSource);
    }

    @Test
    @DisplayName("Order를 저장한다.")
    void save() {
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(10, true));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(10, true));
        tableGroupRepository.save(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

        Order order = orderDao.save(new Order(orderTable1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()));
        assertThat(order).isEqualTo(orderDao.findById(order.getId()).orElseThrow());
    }

    @Test
    @DisplayName("모든 Order를 조회한다.")
    void findAll() {
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(10, true));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(10, true));
        tableGroupRepository.save(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

        Order order1 = orderDao.save(new Order(orderTable1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()));
        Order order2 = orderDao.save(new Order(orderTable2.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()));

        List<Order> orders = orderDao.findAll();
        assertThat(orders).containsExactly(order1, order2);
    }

    @Test
    @DisplayName("OrderTableId와 OrderStatus 내에 Order가 존재하는지 확인한다.")
    void existsByOrderTableIdAndOrderStatusIn() {
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(10, true));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(10, true));
        tableGroupRepository.save(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

        orderDao.save(new Order(orderTable1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()));

        boolean actual = orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable1.getId(),
                List.of(OrderStatus.COOKING.name()));

        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("OrderTableId 중 하나 이상의 id와 OrderStatus 중 하나 이상의 상태에 Order가 존재하는지 확인한다.")
    void existsByOrderTableIdInAndOrderStatusIn() {
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(10, true));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(10, true));
        tableGroupRepository.save(new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2)));

        orderDao.save(new Order(orderTable1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()));

        boolean actual = orderDao.existsByOrderTableIdInAndOrderStatusIn(List.of(orderTable1.getId()),
                List.of(OrderStatus.COOKING.name()));

        assertThat(actual).isTrue();
    }
}
