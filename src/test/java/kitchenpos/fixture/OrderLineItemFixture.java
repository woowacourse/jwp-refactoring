package kitchenpos.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.ui.dto.request.OrderLineItemRequest;
import kitchenpos.ui.dto.response.OrderLineItemResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class OrderLineItemFixture {

    private OrderLineItemRepository orderLineItemRepository;

    public OrderLineItemFixture(OrderLineItemRepository orderLineItemRepository) {
        this.orderLineItemRepository = orderLineItemRepository;
    }

    public OrderLineItemRequest 주문_메뉴_생성_요청(Long menuId, Long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public List<OrderLineItemRequest> 주문_메뉴_요청_리스트_생성(OrderLineItemRequest... orderLineItemRequests) {
        return Arrays.asList(orderLineItemRequests);
    }

    public List<OrderLineItemResponse> 주문_메뉴_리스트_생성(OrderLineItemResponse... orderLineItemResponses) {
        return Arrays.asList(orderLineItemResponses);
    }

    public List<OrderLineItem> 특정_주문에_따른_주문_메뉴들_조회(Order order){
        return orderLineItemRepository.findAllByOrder(order);
    }
}
