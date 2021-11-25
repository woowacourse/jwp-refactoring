package kitchenpos.order.fixture;

import java.util.Arrays;
import java.util.List;
import kitchenpos.DefaultFixture;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.ui.dto.request.OrderChangeStatusRequest;
import kitchenpos.order.ui.dto.request.OrderCreatedRequest;
import kitchenpos.order.ui.dto.request.OrderLineItemRequest;
import kitchenpos.order.ui.dto.response.OrderResponse;
import kitchenpos.testtool.RequestBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class OrderFixture extends DefaultFixture {

    private final OrderRepository orderRepository;

    public OrderFixture(RequestBuilder requestBuilder, OrderRepository orderRepository) {
        super(requestBuilder);
        this.orderRepository = orderRepository;
    }

    public OrderCreatedRequest 주문_생성_요청(
            Long orderTableId,
            List<OrderLineItemRequest> orderLineItems
    ) {
        return new OrderCreatedRequest(orderTableId, orderLineItems);
    }

    public List<OrderResponse> 주문_응답_리스트_생성(OrderResponse... orderResponses) {
        return Arrays.asList(orderResponses);
    }

    public Order 주문_조회(Long id) {
        return orderRepository.getOne(id);
    }

    public OrderResponse 주문_등록(OrderCreatedRequest request) {
        return request()
                .post("/api/orders", request)
                .build()
                .convertBody(OrderResponse.class);
    }

    public List<OrderResponse> 주문_리스트_조회() {
        return request()
                .get("/api/orders")
                .build()
                .convertBodyToList(OrderResponse.class);
    }

    public OrderResponse 주문_상태_변경(Long orderId, OrderChangeStatusRequest request) {
        return request()
                .put("/api/orders/" + orderId + "/order-status", request)
                .build()
                .convertBody(OrderResponse.class);
    }
}
