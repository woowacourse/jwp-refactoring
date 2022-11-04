package kitchenpos.application.mapper;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderedMenu;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderMapper(final OrderTableRepository orderTableRepository, final MenuRepository menuRepository) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    public Order from(final OrderRequest orderRequest) {
        OrderTable orderTable = getOrderTable(orderRequest.getOrderTableId());
        List<OrderLineItem> orderLineItems = mapToOrderLineItems(orderRequest.getOrderLineItems());

        return new Order(orderTable, orderLineItems);
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<OrderLineItem> mapToOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> {
                            Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                                    .orElseThrow(IllegalAccessError::new);
                            return new OrderLineItem(
                                    new OrderedMenu(menu.getId(), menu.getName(), menu.getPrice().getValue()),
                                    orderLineItemRequest.getQuantity()
                            );
                        }
                )
                .collect(Collectors.toList());
    }
}
