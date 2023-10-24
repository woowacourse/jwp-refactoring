package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.order.MenuQuantityDto;
import kitchenpos.application.dto.order.OrderRequest;
import kitchenpos.application.dto.order.OrderResponse;
import kitchenpos.application.dto.order.OrderStatusChangeRequest;
import kitchenpos.domain.Menu;
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

        final Order order = Order.createDefault(orderTable, LocalDateTime.now());
        final List<OrderLineItem> orderLineItems = convertToOrderLineItems(orderRequest.getOrderLineItems(), order);

        order.addOrderLineItems(orderLineItems);
        orderRepository.save(order);
        orderLineItemRepository.saveAll(orderLineItems);

        return OrderResponse.from(order);
    }

    private List<OrderLineItem> convertToOrderLineItems(final List<MenuQuantityDto> menuQuantities, final Order order) {
        return menuQuantities
            .stream()
            .map(menuIdWithQuantity ->
                new OrderLineItem(order, getMenuFromMenuIdQuantityDto(menuIdWithQuantity), menuIdWithQuantity.getQuantity())
            )
            .collect(Collectors.toList());
    }

    private Menu getMenuFromMenuIdQuantityDto(final MenuQuantityDto menuQuantityDto) {
        return menuRepository.findById(menuQuantityDto.getMenuId())
            .orElseThrow(IllegalArgumentException::new);
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
