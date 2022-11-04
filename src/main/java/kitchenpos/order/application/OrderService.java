package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusRequest;
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
    public Order create(final OrderRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        final List<OrderLineItem> orderLineItems = findOrderLineItems(request.getOrderLineItems());
        final Order order = request.toEntity(orderLineItems);
        orderTable.add(order);
        return order;
    }

    private List<OrderLineItem> findOrderLineItems(final List<OrderLineItemRequest> orderLineItemsRequest) {
        final List<Long> menuIds = orderLineItemsRequest.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
        final List<Menu> savedMenus = menuRepository.findAllByIdIn(menuIds);
        if (menuIds.size() != savedMenus.size()) {
            throw new IllegalArgumentException();
        }
        return orderLineItemsRequest.stream()
                .map(it -> it.toEntity(matchMenu(it.getMenuId(), savedMenus)))
                .collect(Collectors.toList());
    }

    private Menu matchMenu(final Long menuId, final List<Menu> savedMenus) {
        return savedMenus.stream()
                .filter(it -> it.getId().equals(menuId))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrder.updateOrderStatus(request.getOrderStatus());
        orderRepository.save(savedOrder);
        return savedOrder;
    }
}
