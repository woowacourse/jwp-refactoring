package kitchenpos.fixture;

import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.testtool.RequestBuilder;
import kitchenpos.order.ui.dto.request.OrderLineItemRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class OrderLineItemFixture extends DefaultFixture {

    private OrderLineItemRepository orderLineItemRepository;

    public OrderLineItemFixture(RequestBuilder requestBuilder,
            OrderLineItemRepository orderLineItemRepository) {
        super(requestBuilder);
        this.orderLineItemRepository = orderLineItemRepository;
    }

    public OrderLineItemRequest 주문_메뉴_생성_요청(Long menuId, Long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public List<OrderLineItemRequest> 주문_메뉴_요청_리스트_생성(
            OrderLineItemRequest... orderLineItemRequests) {
        return Arrays.asList(orderLineItemRequests);
    }

    public List<OrderLineItem> 특정_주문에_따른_주문_메뉴들_조회(Order order) {
        return orderLineItemRepository.findAllByOrder(order);
    }
}
