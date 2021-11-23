package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.repository.OrderRepository;
import kitchenpos.ui.dto.request.OrderCreatedRequest;
import kitchenpos.ui.dto.request.OrderLineItemRequest;
import kitchenpos.ui.dto.response.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class OrderFixture {

    private final OrderRepository orderRepository;

    public OrderFixture(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderCreatedRequest 주문_생성_요청(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return new OrderCreatedRequest(orderTableId, orderLineItems);
    }

    public List<OrderResponse> 주문_응답_리스트_생성(OrderResponse... orderResponses) {
        return Arrays.asList(orderResponses);
    }

    public Order 주문_조회(Long id) {
        return orderRepository.getOne(id);
    }
}
