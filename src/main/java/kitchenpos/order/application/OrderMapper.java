package kitchenpos.order.application;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.dto.OrderCreationRequest;
import kitchenpos.order.application.dto.OrderItemsWithQuantityRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderValidator;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private final MenuRepository menuRepository;

    public OrderMapper(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Order from(final OrderCreationRequest request, final OrderValidator orderValidator) {
        final Long orderTableId = request.getOrderTableId();
        final List<OrderLineItem> orderLineItems = getOrderLineItemsByRequest(request.getOrderLineItems());
        return new Order(orderTableId, orderLineItems, orderValidator);
    }

    private List<OrderLineItem> getOrderLineItemsByRequest(
            final List<OrderItemsWithQuantityRequest> orderLineItemRequests
    ) {
        final List<Long> menuIds = extractMenuIds(orderLineItemRequests);
        final Map<Long, Menu> menusByMenuId = menuRepository.findAllByIdIn(menuIds).stream().collect(Collectors.toMap(
                Menu::getId, Function.identity()
        ));
        validateExistMenus(menuIds, menusByMenuId.values());
        return orderLineItemRequests.stream().map(request -> {
            final Menu menu = menusByMenuId.get(request.getMenuId());
            return new OrderLineItem(menu.getName(), menu.getPrice(), request.getQuantity());
        }).collect(Collectors.toList());
    }

    private List<Long> extractMenuIds(final List<OrderItemsWithQuantityRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(OrderItemsWithQuantityRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public void validateExistMenus(final List<Long> menuIds, final Collection<Menu> menus) {
        if (menus.isEmpty() || !Objects.equals(menus.size(), menuIds.size())) {
            throw new IllegalArgumentException("Menu does not exist.");
        }
    }
}
