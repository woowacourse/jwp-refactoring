package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusChangeRequest;
import kitchenpos.dto.orderlineitem.OrderLineItemCreateRequest;
import kitchenpos.exception.*;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(OrderCreateRequest orderCreateRequest) {
        List<OrderLineItemCreateRequest> orderLineItemCreateRequests =
                orderCreateRequest.getOrderLineItemCreateRequests();
        validateOrderLineItemCreateRequests(orderLineItemCreateRequests);

        Long orderTableId = orderCreateRequest.getOrderTableId();
        OrderTable orderTable =
                orderTableRepository.findById(orderTableId).orElseThrow(() -> new OrderTableNotFoundException(orderTableId));
        validateOrderTable(orderTable);

        Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        List<OrderLineItem> savedOrderLineItems = createOrderLineItems(orderLineItemCreateRequests, order);
        savedOrder.setOrderLineItems(savedOrderLineItems);

        return OrderResponse.from(savedOrder);
    }

    private void validateOrderLineItemCreateRequests(List<OrderLineItemCreateRequest> orderLineItemCreateRequests) {
        if (CollectionUtils.isEmpty(orderLineItemCreateRequests)) {
            throw new InvalidOrderLineItemCreateRequestsException("주문 항목이 없습니다!");
        }

        final List<Long> menuIds = orderLineItemCreateRequests.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemCreateRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new InvalidOrderLineItemCreateRequestsException("주문 항목 내에서 중복되는 메뉴가 없어야 합니다!");
        }
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new EmptyOrderTableException();
        }
    }

    private List<OrderLineItem> createOrderLineItems(List<OrderLineItemCreateRequest> orderLineItemCreateRequests,
                                                     Order order) {
        List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItemCreateRequest orderLineItemCreateRequest : orderLineItemCreateRequests) {
            Long menuId = orderLineItemCreateRequest.getMenuId();
            Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new MenuNotFoundException(menuId));
            OrderLineItem orderLineItem = new OrderLineItem(order, menu, orderLineItemCreateRequest.getQuantity());
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }

        return savedOrderLineItems;
    }

    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        orders.forEach(order -> order.setOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId())));

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatusChangeRequest orderStatusChangeRequest) {
        Order savedOrder = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        OrderStatus orderStatus = OrderStatus.from(orderStatusChangeRequest.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

        return OrderResponse.from(savedOrder);
    }
}
