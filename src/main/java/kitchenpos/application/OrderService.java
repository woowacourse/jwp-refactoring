package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.order.MenuQuantityDto;
import kitchenpos.application.dto.order.OrderRequest;
import kitchenpos.application.dto.order.OrderResponse;
import kitchenpos.application.dto.order.OrderStatusChangeRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        final MenuRepository menuRepository,
        final OrderRepository orderRepository,
        final OrderLineItemRepository orderLineItemRepository,
        final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        final List<OrderLineItem> orderLineItems = convertToOrderLineItems(orderRequest.getOrderLineItems());
        final Order order = Order.createDefault(orderTable, LocalDateTime.now(), orderLineItems);
        orderRepository.save(order);

        return OrderResponse.from(order);
    }

    private List<OrderLineItem> convertToOrderLineItems(final List<MenuQuantityDto> menuQuantities) {
        validateAllMenusAvailable(menuQuantities);

        return menuQuantities
            .stream()
            .map(menuIdWithQuantity ->
                new OrderLineItem(menuIdWithQuantity.getMenuId(), menuIdWithQuantity.getQuantity())
            )
            .collect(Collectors.toList());
    }

    private void validateAllMenusAvailable(final List<MenuQuantityDto> menuQuantities) {
        final List<Long> menuIds = menuQuantities.stream()
            .map(MenuQuantityDto::getMenuId)
            .collect(Collectors.toList());

        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("잘못된 메뉴가 주문에 포함되었습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest changeRequest) {
        final Order order = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.from(changeRequest.getOrderStatus());
        order.changeOrderStatus(orderStatus);

        return OrderResponse.from(order);
    }
}
