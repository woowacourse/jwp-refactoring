package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.menu.Menu;
import kitchenpos.dto.order.OrderChangeStatusRequest;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrdersResponse;
import kitchenpos.exception.MenuException.NotFoundMenuException;
import kitchenpos.exception.OrderException;
import kitchenpos.exception.OrderTableException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository,
                        final OrderLineItemRepository orderLineItemRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();
        final List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
             throw new OrderException.NotFoundOrderLineItemMenuExistException();
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(OrderTableException.NotFoundOrderTableException::new);
        if (orderTable.isEmpty()) {
            throw new OrderException.CannotOrderStateByOrderTableEmptyException();
        }

        final Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now());
        final Order savedOrder = orderRepository.save(order);
        associateOrderLineItem(orderLineItemRequests, savedOrder);

        return OrderResponse.from(savedOrder);
    }

    private void associateOrderLineItem(final List<OrderLineItemRequest> orderLineItemRequests, final Order savedOrder) {
        for (final OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            final Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(NotFoundMenuException::new);
            savedOrder.confirmOrderLineItem(menu, orderLineItemRequest.getQuantity());
        }
    }

    public OrdersResponse list() {
        return OrdersResponse.from(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(OrderException.NotFoundOrderException::new);

        savedOrder.validateAvailableChangeStatus();
        savedOrder.changeStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return OrderResponse.from(savedOrder);
    }
}
