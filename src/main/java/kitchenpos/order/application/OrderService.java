package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.dto.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderStatusRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public OrderResponse create(final OrderRequest orderRequest) {

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        OrderLineItems orderLineItems = getOrderLineItems(orderRequest);
        Order order = new Order(orderTable.getId(), orderLineItems);

        return OrderResponse.from(orderRepository.save(order));
    }

    private OrderLineItems getOrderLineItems(OrderRequest orderRequest) {
        final List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems()
                .stream()
                .map(OrderLineItemRequest::toOrderLineItem)
                .collect(Collectors.toList());

        List<Menu> menus = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .map(AggregateReference::getId)
                .collect(Collectors.collectingAndThen(Collectors.toList(), menuRepository::findAllByIdIn));

        return OrderLineItems.of(orderLineItems, menus);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        order.updateOrderStatus(orderStatusRequest.getOrderStatus());

        return OrderResponse.from(orderRepository.save(order));
    }
}
