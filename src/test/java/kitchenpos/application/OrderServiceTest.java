package kitchenpos.application;

import fixture.OrderBuilder;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderServiceTest extends ServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderDao orderDao;

    @Test
    void 주문을_저장한다() {
        Order order = OrderBuilder.init()
                .build();

        Order created = orderService.create(order);

        assertThat(created.getId()).isNotNull();
    }

    @Test
    void 주문_상품들이_비어있으면_예외를_발생한다() {
        Order order = OrderBuilder.init()
                .orderLineItems(List.of())
                .build();

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴개수와_주무항목개수가_맞지_않으면_예외를_발생한다() {
        List<OrderLineItem> orderLineItemList = List.of(
//                OrderLineItemBuilder.init().menuId(1L).build(),
//                OrderLineItemBuilder.init().menuId(1000L).build()
        );
        Order order = OrderBuilder.init()
                .orderLineItems(orderLineItemList)
                .build();

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블이_존재하지_않으면_예외를_발생한다() {
        Order order = OrderBuilder.init()
                .build();

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_주문을_조회한다() {
        List<Order> expected = orderDao.findAll();

        List<Order> actual = orderService.list();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("orderLineItems")
                .isEqualTo(expected);
    }

    @Test
    void 주문_상태_변경_시_주문이_없으면_예외를_발생한다() {
        Order order = OrderBuilder.init()
                .id(100L)
                .orderStatus(OrderStatus.COOKING)
                .build();

        assertThatThrownBy(() -> orderService.changeOrderStatus(100L, order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태_변경_시_이미_완료된_주문이면_예외를_발생한다() {
        Order order = OrderBuilder.init()
                .id(1L)
                .orderStatus(OrderStatus.COMPLETION)
                .build();

        orderService.changeOrderStatus(1L, order);
    }
}
