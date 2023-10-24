//package kitchenpos.application;
//
//import kitchenpos.dao.MenuDao;
//import kitchenpos.dao.OrderTableDao;
//import kitchenpos.domain.*;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//import static kitchenpos.fixture.MenuFixture.MENU_1;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@ServiceTest
//class OrderServiceTest {
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private MenuDao menuDao;
//
//    @Autowired
//    private OrderTableDao orderTableDao;
//
//    @Test
//    void 주문을_생성한다() {
//        // given
//        Menu menu = menuDao.save(MENU_1);
//        OrderTable orderTable = orderTableDao.save(new OrderTable(3, false));
//        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 3);
//        Order order = new Order(orderTable.getId(), List.of(orderLineItem));
//
//        // when
//        Order savedOrder = orderService.create(order);
//
//        // then
//        assertAll(
//                () -> assertEquals(savedOrder.getOrderStatus(), OrderStatus.COOKING.name()),
//                () -> assertThat(savedOrder.getOrderLineItems()).usingRecursiveComparison()
//                        .ignoringFields("seq")
//                        .isEqualTo(List.of(orderLineItem)));
//    }
//
//    @Test
//    void empty_상태의_테이블에_대한_주문_생성은_예외_발생() {
//        // given
//        Menu menu = menuDao.save(MENU_1);
//        OrderTable orderTable = orderTableDao.save(new OrderTable(3, true));
//        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 3);
//        Order order = new Order(orderTable.getId(), List.of(orderLineItem));
//
//        // when, then
//        assertThatThrownBy(() -> orderService.create(order))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 같은_메뉴에_대한_주문_항목이_중복되어_존재한다면_주문_생성시_예외_발생() {
//        // given
//        Menu menu = menuDao.save(MENU_1);
//        OrderTable orderTable = orderTableDao.save(new OrderTable(3, true));
//        OrderLineItem orderLineItem1 = new OrderLineItem(menu.getId(), 3);
//        OrderLineItem orderLineItem2 = new OrderLineItem(menu.getId(), 4);
//        Order order = new Order(orderTable.getId(), List.of(orderLineItem1, orderLineItem2));
//
//        // when, then
//        assertThatThrownBy(() -> orderService.create(order))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 전체_주문을_조회한다() {
//        // given
//        Menu menu = menuDao.save(MENU_1);
//        OrderTable orderTable = orderTableDao.save(new OrderTable(3, false));
//        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 3);
//        Order order = new Order(orderTable.getId(), List.of(orderLineItem));
//
//        Order savedOrder = orderService.create(order);
//
//        // when
//        List<Order> orders = orderService.list();
//
//        // then
//        assertThat(orders).usingRecursiveComparison()
//                .isEqualTo(List.of(savedOrder));
//    }
//
//    @Test
//    void 주문_상태를_변경한다() {
//        // given
//        Menu menu = menuDao.save(MENU_1);
//        OrderTable orderTable = orderTableDao.save(new OrderTable(3, false));
//        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 3);
//        Order order = new Order(orderTable.getId(), List.of(orderLineItem));
//
//        Order savedOrder = orderService.create(order);
//
//        // when
//        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(),
//                new Order(null, null, OrderStatus.MEAL.name(), null, null));
//
//        // then
//        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
//    }
//
//    @Test
//    void 주문_상태가_완료인_주문은_주문_상태를_변경할_수_없다() {
//        // given
//        Menu menu = menuDao.save(MENU_1);
//        OrderTable orderTable = orderTableDao.save(new OrderTable(3, false));
//        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 3);
//        Order order = new Order(orderTable.getId(), List.of(orderLineItem));
//
//        Order savedOrder = orderService.create(order);
//        orderService.changeOrderStatus(savedOrder.getId(),
//                new Order(null, null, OrderStatus.COMPLETION.name(), null, null));
//
//        // when, then
//        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(),
//                new Order(null, null, OrderStatus.MEAL.name(), null, null)))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//}
