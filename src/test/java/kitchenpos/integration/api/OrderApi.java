package kitchenpos.integration.api;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.integration.utils.MockMvcResponse;
import kitchenpos.integration.utils.MockMvcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderApi {

    private static final String BASE_URL = "/api/orders";

    @Autowired
    private MockMvcUtils mockMvcUtils;

    public MockMvcResponse<Order> 주문(Order order) {
        return mockMvcUtils.request()
            .post(BASE_URL)
            .content(order)
            .asSingleResult(Order.class);
    }

    public MockMvcResponse<Order> 주문_상태_변경(Long orderId, OrderStatus orderStatus) {
        final Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        return mockMvcUtils.request()
            .put(BASE_URL + "/{orderId}/order-status", orderId)
            .content(order)
            .asSingleResult(Order.class);
    }
}
