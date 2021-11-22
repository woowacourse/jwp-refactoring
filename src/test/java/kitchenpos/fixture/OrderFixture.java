package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.repository.OrderRepository;
import kitchenpos.ui.dto.request.OrderCreatedRequest;
import kitchenpos.ui.dto.request.OrderLineItemRequest;
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

    public Order 주문_생성(Long id, Long orderTableId, String orderStatus, List<OrderLineItem> orderLineItems) {
        return null;
    }

    public List<Order> 주문_리스트_생성(Order... orders) {
        return Arrays.asList(orders);
    }

    public Order 주문_조회(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}
