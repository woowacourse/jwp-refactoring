package kitchenpos.application;

import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderStatusChangeRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.application.mapper.OrderMapper;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final kitchenpos.order.application.OrderTableService orderTableService;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final kitchenpos.order.application.OrderTableService orderTableService) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {
        checkOrderTableExists(orderCreateRequest);
        checkAllMenuExists(orderCreateRequest);
        final List<OrderLineItem> orderLineItems = makeOrderLineItems(orderCreateRequest);
        final Order order = new Order(orderCreateRequest.getOrderTableId(), new OrderLineItems(orderLineItems));
        final Order savedOrder = orderRepository.save(order);
        return OrderMapper.mapToResponse(savedOrder);
    }

    private void checkOrderTableExists(final OrderCreateRequest orderCreateRequest) {
        if (orderTableService.isOrderTableNotExist(orderCreateRequest.getOrderTableId())) {
            throw new IllegalArgumentException();
        }
    }

    private void checkAllMenuExists(final OrderCreateRequest orderCreateRequest) {
        final List<Long> menuIds = orderCreateRequest.getOrderLineItems()
                .stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
        if (!menuRepository.existsAllByIdIn((menuIds))) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderLineItem> makeOrderLineItems(final OrderCreateRequest orderCreateRequest) {
        return orderCreateRequest.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
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
        savedOrder.changeOrderStatus(orderStatus);
        return OrderMapper.mapToResponse(orderRepository.save(savedOrder));
    }
}
