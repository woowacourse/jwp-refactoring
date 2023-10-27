package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderCreationOrderLineItemValidator implements OrderCreationValidator {

    private final MenuRepository menuRepository;

    public OrderCreationOrderLineItemValidator(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public void validate(final Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();
        validateEmptinessOf(orderLineItems);

        if (hasInvalidMenuId(getMenuIds(orderLineItems))) {
            throw new IllegalArgumentException();
        }
    }

    private static void validateEmptinessOf(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    private static List<Long> getMenuIds(final List<OrderLineItem> orderLineItems) {
        return orderLineItems
                .stream().map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    private boolean hasInvalidMenuId(final List<Long> menuIds) {
        return menuIds.size() != menuRepository.countByIdIn(menuIds);
    }
}
