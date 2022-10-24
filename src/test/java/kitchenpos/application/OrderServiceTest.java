package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import javax.sql.DataSource;
import kitchenpos.BeanAssembler;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class OrderServiceTest {

    private OrderService orderService;
    private TableGroupDao tableGroupDao;
    private OrderTableDao orderTableDao;
    private OrderDao orderDao;

    @Autowired
    public OrderServiceTest(DataSource dataSource) {
        this.orderService = BeanAssembler.createOrderService(dataSource);
        this.tableGroupDao = BeanAssembler.createTableGroupDao(dataSource);
        this.orderTableDao = BeanAssembler.createOrderTableDao(dataSource);
        this.orderDao = BeanAssembler.createOrderDao(dataSource);
    }

    @Test
    void create() {
        // given
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 3, false));
        Order order = new Order(orderTable.getId(), null, LocalDateTime.now(), List.of(new OrderLineItem(null, 1L, 2)));

        // when
        Order createdOrder = orderService.create(order);

        // then
        assertThat(createdOrder.getId()).isNotNull();
    }

    @Test
    void createWithEmptyOrderLineItems() {
        // given
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 3, false));
        Order order = new Order(orderTable.getId(), null, LocalDateTime.now(), List.of());

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createWithInvalidMenu() {
        // given
        long invalidMenuId = 999L;
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 3, false));
        Order order = new Order(orderTable.getId(), null, LocalDateTime.now(),
                List.of(new OrderLineItem(null, invalidMenuId, 2)));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createWithInvalidOrderTable() {
        // given
        long invalidOrderTableId = 999L;
        Order order = new Order(invalidOrderTableId, null, LocalDateTime.now(),
                List.of(new OrderLineItem(null, 1L, 2)));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createWithEmptyOrderTable() {
        // given
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 0, true));
        Order order = new Order(orderTable.getId(), null, LocalDateTime.now(), List.of(new OrderLineItem(null, 1L, 2)));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        // given & when
        List<Order> orders = orderService.list();
        // then
        assertThat(orders).isEmpty();
    }

    @Test
    void changeOrderStatus() {
        // given
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 3, false));
        Order createdOrder = orderService.create(
                new Order(orderTable.getId(), null, LocalDateTime.now(), List.of(new OrderLineItem(null, 1L, 2)))
        );

        // when
        Order changedOrder = orderService.changeOrderStatus(createdOrder.getId(),
                new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), null)
        );

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void changeOrderStatusWithInvalidOrder() {
        // given
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 3, false));
        long invalidOrderId = 999L;

        // when
        assertThatThrownBy(() -> orderService.changeOrderStatus(invalidOrderId,
                new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), null)
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeOrderStatusWithAlreadyCompletedStatus() {
        // given
        String orderStatus = OrderStatus.COMPLETION.name();

        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 3, false));
        Order createdOrder = orderDao.save(new Order(
                orderTable.getId(),
                orderStatus,
                LocalDateTime.now(),
                List.of(new OrderLineItem(null, 1L, 2))
        ));

        // when
        assertThatThrownBy(() -> orderService.changeOrderStatus(createdOrder.getId(),
                new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), null)
        )).isInstanceOf(IllegalArgumentException.class);
    }
}
