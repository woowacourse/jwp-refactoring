package kitchenpos.application;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderRequest;

import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceTest extends ServiceTest {

    private final OrderRequest cookingOrder;

    public OrderServiceTest() {
        this.cookingOrder = Fixtures.makeOrder();
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
        final Order changedOrder = orderService.changeOrderStatus(savedCookingOrder.getId(), OrderStatus.COMPLETION);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }
}
