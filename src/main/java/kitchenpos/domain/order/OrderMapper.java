package kitchenpos.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private final MenuRepository menuRepository;

    public OrderMapper(final MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Order toOrder(final OrderCreateRequest request) {
        final List<OrderLineItem> orderLineItems = request.getOrderLineItems().stream()
                .map(this::toOrderLineItem)
                .collect(Collectors.toList());
        return new Order(request.getOrderTableId(), OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);
    }

    private OrderLineItem toOrderLineItem(final OrderLineItemRequest request) {
        final Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(IllegalArgumentException::new);
        return new OrderLineItem(menu.getId(), request.getQuantity());
    }
}
