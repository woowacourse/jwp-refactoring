package kitchenpos.application;

import kitchenpos.application.exceptions.NotExistedMenuException;
import kitchenpos.application.exceptions.NotExistedOrderException;
import kitchenpos.application.exceptions.NotExistedOrderTableException;
import kitchenpos.application.exceptions.TableStatusEmptyException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        final OrderTable orderTable = findOrderTable(request);
        final Order savedOrder = orderRepository.save(request.toEntity(orderTable));

        return OrderResponse.of(savedOrder, mapToOrderLineItems(request, savedOrder));
    }

    private OrderTable findOrderTable(final OrderRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(NotExistedOrderTableException::new);

        if (orderTable.isEmpty()) {
            throw new TableStatusEmptyException();
        }
        return orderTable;
    }

    private List<OrderLineItem> mapToOrderLineItems(final OrderRequest request, final Order order) {
        final List<OrderLineItemRequest> orderLineItemsRequest = request.getOrderLineItems();

        return orderLineItemsRequest.stream()
                .map(r -> r.toEntity(
                        order,
                        menuRepository.findById(r.getMenuId())
                                .orElseThrow(NotExistedMenuException::new)
                ))
                .collect(Collectors.toList());
    }

    public OrderResponses list() {
        final List<Order> orders = orderRepository.findAll();
        final List<OrderResponse> orderResponses = orders.stream()
                .map(o -> OrderResponse.of(o, orderLineItemRepository.findAllBy(o.getId())))
                .collect(Collectors.toList());

        return OrderResponses.from(orderResponses);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(NotExistedOrderException::new);

        savedOrder.changeOrderStatus(request.getOrderStatus());

        return OrderResponse.of(savedOrder, mapToOrderLineItems(request, savedOrder));
    }
}
