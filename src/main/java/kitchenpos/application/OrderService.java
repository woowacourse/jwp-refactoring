package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.ui.dto.order.OrderLineItemRequest;
import kitchenpos.ui.dto.order.OrderRequest;
import kitchenpos.ui.dto.order.OrderResponse;
import kitchenpos.ui.dto.order.OrderResponses;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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
    public OrderResponse create(final OrderRequest request) {
        final OrderTable orderTable = findOrderTable(request);
        final Order savedOrder = orderRepository.save(request.toEntity(orderTable));

        savedOrder.setOrderLineItems(mapToOrderLineItems(request, savedOrder));
        return OrderResponse.from(savedOrder);
    }

    private OrderTable findOrderTable(final OrderRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return orderTable;
    }

    private List<OrderLineItem> mapToOrderLineItems(final OrderRequest request, final Order order) {
        final List<OrderLineItemRequest> orderLineItemsRequest = request.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemsRequest)) {
            throw new IllegalArgumentException();
        }

        return orderLineItemsRequest.stream()
                .map(r -> r.toEntity(
                        order,
                        menuRepository.findById(r.getMenuId())
                                .orElseThrow(IllegalArgumentException::new)
                ))
                .collect(Collectors.toList());
    }

    public OrderResponses list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponses.from(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(request.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }
}
