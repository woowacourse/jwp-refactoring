//package kitchenpos.application;
//
//import static kitchenpos.OrderFixtures.createOrder;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import kitchenpos.OrderFixtures;
//import kitchenpos.dao.OrderDao;
//import kitchenpos.dao.OrderTableRepository;
//import kitchenpos.dao.TableGroupRepository;
//import kitchenpos.domain.Order;
//import kitchenpos.domain.OrderLineItem;
//import kitchenpos.domain.OrderStatus;
//import kitchenpos.domain.OrderTable;
//import kitchenpos.domain.TableGroup;
//import kitchenpos.support.ServiceTest;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//@ServiceTest
//class OrderServiceTest {
//
//    private OrderService orderService;
//    private TableGroupRepository tableGroupRepository;
//    private OrderTableRepository orderTableRepository;
//    private OrderDao orderDao;
//
//    @Autowired
//    public OrderServiceTest(
//            OrderService orderService,
//            TableGroupRepository tableGroupRepository,
//            OrderTableRepository orderTableRepository,
//            OrderDao orderDao
//    ) {
//        this.orderService = orderService;
//        this.tableGroupRepository = tableGroupRepository;
//        this.orderTableRepository = orderTableRepository;
//        this.orderDao = orderDao;
//    }
//
//    @Test
//    void create() {
//        // given
//        OrderFixtures.crateOrderRequest();
//        Order order = createOrder();
//
//        // when
//        Order createdOrder = orderService.create(order);
//
//        // then
//        assertThat(createdOrder.getId()).isNotNull();
//    }
//
//    @Test
//    void createWithEmptyOrderLineItems() {
//        // given
//        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), null));
//        OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup, 3, false));
//        Order order = new Order(orderTable.getId(), null, LocalDateTime.now(), List.of());
//
//        // when & then
//        assertThatThrownBy(() -> orderService.create(order))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void createWithInvalidMenu() {
//        // given
//        long invalidMenuId = 999L;
//        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), null));
//        OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup, 3, false));
//        Order order = new Order(orderTable.getId(), null, LocalDateTime.now(),
//                List.of(new OrderLineItem(null, invalidMenuId, 2)));
//
//        // when & then
//        assertThatThrownBy(() -> orderService.create(order))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void createWithInvalidOrderTable() {
//        // given
//        long invalidOrderTableId = 999L;
//        Order order = new Order(invalidOrderTableId, null, LocalDateTime.now(),
//                List.of(new OrderLineItem(null, 1L, 2)));
//
//        // when & then
//        assertThatThrownBy(() -> orderService.create(order))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void createWithEmptyOrderTable() {
//        // given
//        Order order = createOrder();
//
//        // when & then
//        assertThatThrownBy(() -> orderService.create(order))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void list() {
//        // given & when
//        List<Order> orders = orderService.list();
//        // then
//        assertThat(orders).isEmpty();
//    }
//
//    @Test
//    void changeOrderStatus() {
//        // given
//        Order order = createOrder();
//        Order createdOrder = orderDao.save(order);
//        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), null));
//        OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup, 3, false));
//        Order createdOrder = orderService.create(
//                new Order(1L, null, LocalDateTime.now(), List.of(new OrderLineItem(null, 1L, 2)))
//        );
//
//        // when
//        Order changedOrder = orderService.changeOrderStatus(createdOrder.getId(),
//                new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), null)
//        );
//
//        // then
//        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
//    }
//
//    @Test
//    void changeOrderStatusWithInvalidOrder() {
//        // given
//        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), null));
//        OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup, 3, false));
//        long invalidOrderId = 999L;
//
//        // when
//        assertThatThrownBy(() -> orderService.changeOrderStatus(invalidOrderId,
//                new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), null)
//        )).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void changeOrderStatusWithAlreadyCompletedStatus() {
//        // given
//        String orderStatus = OrderStatus.COMPLETION.name();
//
//        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), null));
//        OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup, 3, false));
//        Order createdOrder = orderDao.save(new Order(
//                orderTable.getId(),
//                orderStatus,
//                LocalDateTime.now(),
//                List.of(new OrderLineItem(null, 1L, 2))
//        ));
//
//        // when
//        assertThatThrownBy(() -> orderService.changeOrderStatus(createdOrder.getId(),
//                new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), null)
//        )).isInstanceOf(IllegalArgumentException.class);
//    }
//}
