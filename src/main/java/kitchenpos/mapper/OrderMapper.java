package kitchenpos.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.exception.MenuNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private final MenuRepository menuRepository;

    public OrderMapper(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Order mappingToOrder(final OrderCreateRequest request) {
        List<OrderLineItem> orderLineItems = toOrderLineItems(request.getOrderLineItems());
        return request.toEntity(orderLineItems);
    }

    private List<OrderLineItem> toOrderLineItems(final List<OrderLineItemRequest> requests) {
        return requests.stream()
                .map(this::toOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem toOrderLineItem(final OrderLineItemRequest request) {
        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(MenuNotFoundException::new);
        return new OrderLineItem(menu, request.getQuantity());
    }
}
