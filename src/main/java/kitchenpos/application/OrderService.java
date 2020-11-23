package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.ui.dto.order.OrderLineItemRequest;
import kitchenpos.ui.dto.order.OrderRequest;
import kitchenpos.ui.dto.order.OrderResponse;
import kitchenpos.ui.dto.order.OrderResponses;
import kitchenpos.ui.dto.order.OrderStatusChangeRequest;

@Service
@Transactional(readOnly = true)
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
    public OrderResponse create(final OrderRequest request) {
        OrderTable orderTable = findOrderTable(request);
        Order order = request.toEntity(orderTable);
        Order savedOrder = orderRepository.save(order);
        List<OrderLineItem> orderLineItems = mapToOrderLineItems(request, order);

        return OrderResponse.of(savedOrder, orderLineItems);
    }

    private List<OrderLineItem> mapToOrderLineItems(OrderRequest request, Order order) {
        List<OrderLineItemRequest> orderLineItems = request.getOrderLineItems();

        final List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        return orderLineItems.stream()
            .map(orderLineItemRequest -> orderLineItemRequest.toEntity(order,
                menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(IllegalArgumentException::new)))
            .collect(Collectors.toList());
    }

    private OrderTable findOrderTable(OrderRequest request) {
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블에 주문을 할 수 없습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블에는 주문할 수 없습니다.");
        }
        return orderTable;
    }

    public OrderResponses list() {
        final List<Order> orders = orderRepository.findAll();

        List<OrderResponse> orderResponses = orders.stream()
            .map(order -> OrderResponse.of(order, orderLineItemRepository.findAllByOrderId(order.getId())))
            .collect(Collectors.toList());
        return OrderResponses.from(orderResponses);
    }

    @Transactional
    public void changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeStatus(request.getOrderStatus());
    }
}
