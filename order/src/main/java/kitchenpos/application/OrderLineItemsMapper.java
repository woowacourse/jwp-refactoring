package kitchenpos.application;

import kitchenpos.application.dto.request.CreateOrderRequest;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.OrderLineItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderLineItemsMapper {

    private final MenuRepository menuRepository;

    public OrderLineItemsMapper(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<OrderLineItem> toOrderLineItems(CreateOrderRequest createOrderRequest, OrderLineItemValidator validator) {
        List<OrderLineItemRequest> orderLineItemsRequests = createOrderRequest.getOrderLineItems();
        List<Long> menuIds = orderLineItemsRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
        long orderMenuSize = menuRepository.countByIdIn(menuIds);
        validator.validate(orderLineItemsRequests, orderMenuSize);

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest OrderLineItemRequests : orderLineItemsRequests) {
            Long menuId = OrderLineItemRequests.getMenuId();
            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(IllegalArgumentException::new);
            orderLineItems.add(new OrderLineItem(OrderLineItemRequests.getQuantity(), menu.getName(), menu.getPrice()));
        }
        return orderLineItems;
    }
}
