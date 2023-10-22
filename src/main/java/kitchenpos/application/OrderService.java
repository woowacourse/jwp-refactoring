package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.dto.order.OrderCreateRequest;
import kitchenpos.application.dto.order.OrderCreateRequest.OrderLineItemCreateRequest;
import kitchenpos.application.dto.order.OrderCreateResponse;
import kitchenpos.application.dto.order.OrderResponse;
import kitchenpos.application.dto.order.OrderStatusRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.persistence.MenuRepository;
import kitchenpos.persistence.OrderRepository;
import kitchenpos.persistence.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
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

    public OrderCreateResponse create(final OrderCreateRequest request) {
        final List<OrderLineItemCreateRequest> requestOrderLineItems = request.getOrderLineItems();

        if (CollectionUtils.isEmpty(requestOrderLineItems)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = requestOrderLineItems.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(toList());

        if (requestOrderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final OrderLineItemCreateRequest requestOrderLineItem : requestOrderLineItems) {
            final Menu menu = menuRepository.findById(requestOrderLineItem.getMenuId()).get();
            final OrderLineItem orderLineItem = new OrderLineItem(menu, requestOrderLineItem.getQuantity());
            orderLineItems.add(orderLineItem);
        }
        final Order order = new Order(orderTable, OrderStatus.COOKING, orderLineItems);
        final Order savedOrder = orderRepository.save(order);

        return OrderCreateResponse.of(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::of)
                .collect(toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus().name());
        savedOrder.changeOrderStatus(orderStatus);

        return OrderResponse.of(savedOrder);
    }
}
