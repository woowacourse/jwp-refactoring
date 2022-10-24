package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class OrderServiceTest {

    private final OrderService orderService;

    OrderServiceTest(OrderService orderService) {
        this.orderService = orderService;
    }

    @Test
    void 주문을_생성한다() {
        Order order = new Order(9L, "NONE", LocalDateTime.now(), List.of(new OrderLineItem(1L, 1L, 1L, 1)));

        assertThat(orderService.create(order)).isInstanceOf(Order.class);
    }

    @Test
    void 주문정보가_없는_경우_예외를_발생시킨다() {
        Order order = new Order(9L, "NONE", LocalDateTime.now(), null);

        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문정보와_저장된_메뉴와_다를_경우_예외를_발생시킨다() {
        Order order = new Order(9L, "NONE", LocalDateTime.now(), List.of(new OrderLineItem(1L, 1L, -1L, 1)));

        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블이_존재하지_않는_경우_예외를_발생시킨다() {
        Order order = new Order(-1L, "NONE", LocalDateTime.now(), List.of(new OrderLineItem(1L, 1L, 1L, 1)));

        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블이_빈_경우_예외를_발생시킨다() {
        Order order = new Order(1L, "NONE", LocalDateTime.now(), List.of(new OrderLineItem(1L, 1L, 1L, 1)));

        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_조회한다() {
        Order order = new Order(9L, "NONE", LocalDateTime.now(), List.of(new OrderLineItem(1L, 1L, 1L, 1)));
        orderService.create(order);

        List<Order> orders = orderService.list();

        assertThat(orders).hasSize(5);
    }

    @Test
    void 주문의_상태를_조리에서_식사로_바꾼다() {
        Order order = new Order(9L, "NONE", LocalDateTime.now(), List.of(new OrderLineItem(1L, 1L, 1L, 1)));
        orderService.create(order);

        Order foundOrder = orderService.list().get(0);
        foundOrder.setOrderStatus("MEAL");
        Order result = orderService.changeOrderStatus(1L, foundOrder);

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void 주문의_상태를_바꿀때_주문이_존재하지_않는_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문의_상태를_바꿀때_주문의_상태가_계산완료인_경우_예외를_발생시킨다() {
        Order order = new Order(9L, "COMPLETION", LocalDateTime.now(), List.of(new OrderLineItem(1L, 1L, 1L, 1)));

        assertThatThrownBy(() -> orderService.changeOrderStatus(3L, order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
