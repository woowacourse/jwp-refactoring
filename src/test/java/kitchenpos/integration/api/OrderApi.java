package kitchenpos.integration.api;

import kitchenpos.application.response.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.integration.utils.MockMvcResponse;
import kitchenpos.integration.utils.MockMvcUtils;
import kitchenpos.ui.request.OrderCreateRequest;
import kitchenpos.ui.request.OrderStatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderApi {

    private static final String BASE_URL = "/api/orders";

    @Autowired
    private MockMvcUtils mockMvcUtils;

    public MockMvcResponse<OrderResponse> 주문(OrderCreateRequest order) {
        return mockMvcUtils.request()
            .post(BASE_URL)
            .content(order)
            .asSingleResult(OrderResponse.class);
    }

    public MockMvcResponse<OrderResponse> 주문_상태_변경(Long orderId, OrderStatus orderStatus) {
        final OrderStatusRequest orderStatusRequest = OrderStatusRequest.create(orderStatus);
        return mockMvcUtils.request()
            .put(BASE_URL + "/{orderId}/order-status", orderId)
            .content(orderStatusRequest)
            .asSingleResult(OrderResponse.class);
    }
}
