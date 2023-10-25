package kitchenpos.application;

import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderStatusChangeRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.application.mapper.OrderMapper;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        final List<OrderLineItem> orderLineItems = makeOrderLineItems(orderCreateRequest);
        final Order order = OrderMapper.mapToOrder(orderCreateRequest, orderTable, orderLineItems);
        final Order savedOrder = orderRepository.save(order);
        return OrderMapper.mapToResponse(savedOrder);
    }

    private List<OrderLineItem> makeOrderLineItems(final OrderCreateRequest orderCreateRequest) {
        return orderCreateRequest.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItem(findMenuById(it), it.getQuantity()))
                .collect(Collectors.toList());
    }

    private Menu findMenuById(final OrderLineItemRequest it) {
        return menuRepository.findById(it.getMenuId()).orElseThrow(IllegalArgumentException::new);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest orderStatusChangeRequest) {
        final OrderStatus orderStatus = OrderMapper.mapToOrderStatus(orderStatusChangeRequest);
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }
        savedOrder.changeOrderStatus(orderStatus);
        return OrderMapper.mapToResponse(orderRepository.save(savedOrder));
    }
}
