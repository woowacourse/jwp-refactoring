package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.application.dto.OrderChangeRequest;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemDto;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        List<OrderLineItemDto> orderLineItemsDto = request.getOrderLineItems();
        final List<OrderLineItem> orderLineItems = extractOrderLineItemFrom(orderLineItemsDto);
        validateOrderLineItemsCount(orderLineItems);
        final Order savedOrder = orderRepository.save(new Order(
                request.getOrderTableId(),
                orderLineItems
        ));
        return OrderResponse.from(savedOrder);
    }

    private void validateOrderLineItemsCount(final List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAllWithOrderLineItems();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);
        orderRepository.save(savedOrder);
        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> extractOrderLineItemFrom(final List<OrderLineItemDto> orderLineItemsDto) {
        return orderLineItemsDto.stream()
                .map(orderLineItemDto -> {
                    Menu menu = getMenu(orderLineItemDto);
                    return new OrderLineItem(
                            orderLineItemDto.getMenuId(),
                            orderLineItemDto.getMenuId(),
                            menu.getName(),
                            menu.getPrice(),
                            orderLineItemDto.getQuantity()
                    );
                })
                .collect(Collectors.toList());
    }

    private Menu getMenu(final OrderLineItemDto orderLineItemDto) {
        return menuRepository.findById(orderLineItemDto.getMenuId()).orElseThrow(IllegalArgumentException::new);
    }
}
