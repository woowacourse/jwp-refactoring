package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.application.dto.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderStatusChangeRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.exception.OrderException.EmptyOrderLineItemsException;
import kitchenpos.order.domain.exception.OrderException.EmptyOrderTableException;
import kitchenpos.order.domain.exception.OrderException.NotExistsMenuException;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderLineItemRepository orderLineItemRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.getById(orderRequest.getOrderTableId());
        validateOrder(orderRequest.getOrderLineItemsRequest(), orderTable);

        Order savedOrder = orderRepository.save(Order.of(orderTable));

        List<OrderLineItem> orderLineItems = getOrderLineItems(savedOrder, orderRequest);
        orderLineItemRepository.saveAll(orderLineItems);
        return OrderResponse.of(savedOrder, orderTable, orderLineItems);
    }

    private List<OrderLineItem> getOrderLineItems(final Order order, final OrderRequest orderRequest) {
        return orderRequest.getOrderLineItemsRequest().stream()
                .map(orderLineItemRequest -> OrderLineItem.of(order, orderLineItemRequest.getMenuId(),
                        orderLineItemRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    private void validateOrder(final List<OrderLineItemRequest> orderLineItemRequests, final OrderTable orderTable) {
        validateOrderLineItems(orderLineItemRequests);
        validateOrderTable(orderTable);
    }

    private void validateOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests) {
        final List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new EmptyOrderLineItemsException();
        }

        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new NotExistsMenuException();
        }
    }

    private void validateOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new EmptyOrderTableException(orderTable.getId());
        }
    }

    public List<OrderResponse> list() {
        List<OrderResponse> orderResponses = new ArrayList<>();
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrder(order);
            OrderTable orderTable = orderTableRepository.getById(order.getOrderTableId());
            orderResponses.add(OrderResponse.of(order, orderTable, orderLineItems));
        }
        return orderResponses;
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
                                           final OrderStatusChangeRequest orderStatusChangeRequest) {
        final Order order = orderRepository.getById(orderId);

        order.changeOrderStatus(orderStatusChangeRequest.getOrderStatus());

        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrder(order);
        OrderTable orderTable = orderTableRepository.getById(order.getOrderTableId());
        return OrderResponse.of(order, orderTable, orderLineItems);
    }
}
