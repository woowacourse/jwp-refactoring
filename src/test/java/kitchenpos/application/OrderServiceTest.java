//package kitchenpos.application;
//
//import static kitchenpos.fixtures.OrderFixture.ORDER;
//import static kitchenpos.fixtures.OrderLineItemFixture.ORDER_LINE_ITEM;
//import static kitchenpos.fixtures.OrderTableFixture.ORDER_TABLE;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.assertj.core.api.SoftAssertions.assertSoftly;
//
//import java.util.List;
//import kitchenpos.domain.Menu;
//import kitchenpos.domain.Order;
//import kitchenpos.domain.OrderLineItem;
//import kitchenpos.domain.OrderStatus;
//import kitchenpos.domain.OrderTable;
//import kitchenpos.support.TestSupporter;
//import org.junit.jupiter.api.DisplayNameGeneration;
//import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.jdbc.Sql;
//
//@SuppressWarnings("NonAsciiCharacters")
//@DisplayNameGeneration(ReplaceUnderscores.class)
//@Sql("/truncate.sql")
//@SpringBootTest
//class OrderServiceTest {
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private TestSupporter testSupporter;
//
//    @Test
//    void 주문을_생성한다() {
//        // given
//        final Menu menu = testSupporter.createMenu();
//        final OrderLineItem orderLineItem = ORDER_LINE_ITEM(menu, 10L);
//        final OrderTable orderTable = testSupporter.createOrderTable(false);
//        final Order order = ORDER(List.of(orderLineItem), orderTable);
//
//        // when
//        final Order actual = orderService.create(order);
//
//        // then
//        assertSoftly(softAssertions -> {
//            softAssertions.assertThat(actual).usingRecursiveComparison()
//                          .ignoringFields("id", "orderLineItems.seq")
//                          .isEqualTo(order);
//            softAssertions.assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
//        });
//    }
//
//    @Test
//    void 주문을_생성할_때_주문_항목이_null_이라면_예외가_발생한다() {
//        // given
//        final OrderTable orderTable = testSupporter.createOrderTable(false);
//        final Order order = ORDER(null, orderTable);
//
//        // when & then
//        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 주문을_생성할_때_주문_항목_개수와_메뉴의_개수가_같지_않으면_예외가_발생한다() {
//        // given
//        final Menu menu = testSupporter.createMenu();
//        final OrderLineItem orderLineItem1 = ORDER_LINE_ITEM(menu, 10L);
//        final OrderLineItem orderLineItem2 = ORDER_LINE_ITEM(new Menu(), 10L);
//        final OrderTable orderTable = testSupporter.createOrderTable(false);
//        final Order order = ORDER(List.of(orderLineItem1, orderLineItem2), orderTable);
//
//        // when & then
//        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 주문을_생성할_때_주문_테이블이_실재하지_않는다면_예외가_발생한다() {
//        // given
//        final Menu menu = testSupporter.createMenu();
//        final OrderLineItem orderLineItem = ORDER_LINE_ITEM(menu, 10L);
//        final OrderTable orderTable = ORDER_TABLE();
//        final Order order = ORDER(List.of(orderLineItem), orderTable);
//
//        // when & then
//        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 주문을_생성할_때_주문_테이블이_빈_테이블이라면_예외가_발생한다() {
//        // given
//        final Menu menu = testSupporter.createMenu();
//        final OrderLineItem orderLineItem = ORDER_LINE_ITEM(menu, 10L);
//        final OrderTable orderTable = testSupporter.createOrderTable(true);
//        final Order order = ORDER(List.of(orderLineItem), orderTable);
//
//        // when & then
//        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 주문에_대해_전체_조회한다() {
//        // given
//        testSupporter.createOrder();
//
//        // when
//        final List<Order> orders = orderService.list();
//
//        // then
//        assertThat(orders).hasSize(1);
//    }
//
//    @Test
//    void 주문의_주문_상태를_변경한다() {
//        // given
//        final Order order = testSupporter.createOrder();
//        order.setOrderStatus(OrderStatus.MEAL.name());
//
//        // when
//        final Order actual = orderService.changeOrderStatus(order.getId(), order);
//
//        // then
//        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
//    }
//
//    @Test
//    void 주문_상태를_변경할_때_주문이_실재하지_않으면_예외가_발생한다() {
//        // given
//        final Order order = testSupporter.createOrder();
//        order.setOrderStatus(OrderStatus.MEAL.name());
//
//        // when & then
//        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId() + 1, order))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @Test
//    void 주문_상태를_변경할_때_기존_주문의_주문_상태가_COMPLETION_이라면_예외가_발생한다() {
//        // given
//        final Order order = testSupporter.createOrder();
//        order.setOrderStatus(OrderStatus.COMPLETION.name());
//        orderService.changeOrderStatus(order.getId(), order);
//
//        // when & then
//        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order)).isInstanceOf(IllegalArgumentException.class);
//    }
//}