package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order_table.repository.OrderTableRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
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
        List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();
        validateNotEmptyOrderLineItems(orderLineItemRequests);

        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        List<Menu> menus = findMenus(orderLineItemRequests);
        List<OrderLineItem> orderLineItems = convertOrderLineItems(orderLineItemRequests, menus);

        Order order = Order.builder()
            .orderTable(orderTable)
            .orderLineItems(orderLineItems)
            .build();

        Order savedOrder = orderRepository.save(order);
        orderLineItemRepository.saveAll(orderLineItems);

        return OrderResponse.from(savedOrder);
    }

    private void validateNotEmptyOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }
    }

    private List<Menu> findMenus(final List<OrderLineItemRequest> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());
        List<Menu> menus = menuRepository.findAllById(menuIds);
        validateSavedMenu(menuIds, menus);

        return menus;
    }

    private void validateSavedMenu(final List<Long> menuIds, final List<Menu> menus) {
        if (menuIds.size() != menus.size()) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderLineItem> convertOrderLineItems(
        final List<OrderLineItemRequest> requests,
        final List<Menu> menus
    ) {
        return requests.stream()
            .map(request -> convertOrderLineItem(request, menus))
            .collect(Collectors.toList());
    }

    private OrderLineItem convertOrderLineItem(
        final OrderLineItemRequest request,
        final List<Menu> menus
    ) {
        Menu menu = findMenu(request, menus);
        return OrderLineItem.builder()
            .menu(menu)
            .build();
    }

    private Menu findMenu(final OrderLineItemRequest request, final List<Menu> menus) {
        return menus.stream()
            .filter(menu -> menu.isSameId(request.getMenuId()))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return OrderResponse.listFrom(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(
        final Long orderId,
        final OrderStatusChangeRequest request
    ) {
        Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(request.getOrderStatus());

        Order changedOrder = orderRepository.save(savedOrder);
        return OrderResponse.from(changedOrder);
    }
}
