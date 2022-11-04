package kitchenpos.application.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderValidator;
import kitchenpos.domain.order.OrderedMenu;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;
    private final OrderValidator orderValidator;

    public OrderMapper(final OrderTableRepository orderTableRepository, final MenuRepository menuRepository,
                       final OrderValidator orderValidator) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
        this.orderValidator = orderValidator;
    }

    public Order from(final OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItems = mapToOrderLineItems(orderRequest.getOrderLineItems());

        return new Order(orderRequest.getOrderTableId(), orderLineItems, orderValidator);
    }

    private List<OrderLineItem> mapToOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> {
                            Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                                    .orElseThrow(IllegalArgumentException::new);
                            return new OrderLineItem(
                                    new OrderedMenu(menu.getId(), menu.getName(), menu.getPrice().getValue()),
                                    orderLineItemRequest.getQuantity()
                            );
                        }
                )
                .collect(Collectors.toList());
    }
}
