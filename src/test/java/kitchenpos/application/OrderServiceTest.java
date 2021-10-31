package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceTest extends ServiceTest {

    private final Order cookingOrder;
    private final Order completionOrder;

    public OrderServiceTest() {
        final List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(null, null, 1L, 1L));
        this.cookingOrder = new Order(null, 1L, COOKING.name(), LocalDateTime.now(), orderLineItems);
        this.completionOrder = new Order(null, 1L, COMPLETION.name(), LocalDateTime.now(), orderLineItems);
    }

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("주문 생성")
    void createTest() {

        // when
        final Order savedOrder = orderService.create(cookingOrder);

        // then
        assertThat(orderService.list()).contains(savedOrder);
    }

    @Test
    @DisplayName("주문 상태 변경")
    void changeOrderStatusTest() {

        // given
        final Order savedCookingOrder = orderService.create(cookingOrder);

        // when
        final Order changedOrder = orderService.changeOrderStatus(savedCookingOrder.getId(), completionOrder);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(completionOrder.getOrderStatus());
    }
}
