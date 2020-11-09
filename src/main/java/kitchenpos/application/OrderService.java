package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.Menus;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.order.MenuQuantityRequest;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemResponse;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderTableRepository orderTableRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {
        List<MenuQuantityRequest> menuQuantityRequests = orderCreateRequest.getMenuQuantities();
        List<Long> menuIds = menuQuantityRequests.stream()
                .map(MenuQuantityRequest::getMenuId)
                .collect(Collectors.toList());

        List<Menu> savedMenus = menuRepository.findAllById(menuIds);

        Menus menus = new Menus(savedMenus);
        menus.validateMenusCount(menuIds);

        final List<OrderLineItem> unsavedOrderLineItems = makeOrderLineItemsWithoutOrder(menuQuantityRequests, menus);
        final OrderLineItems orderLineItems = new OrderLineItems(unsavedOrderLineItems);

        final OrderTable orderTable = orderTableRepository.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        orderTable.validateNotEmptyTable();

        final Order savedOrder = orderRepository.save(orderCreateRequest.toEntity(orderTable));

        orderLineItems.updateOrder(savedOrder);

        List<OrderLineItem> savedOrderLineItems = orderLineItemRepository.saveAll(orderLineItems.getOrderLineItems());

        return OrderResponse.of(savedOrder, OrderLineItemResponse.ofList(savedOrderLineItems));
    }

    private List<OrderLineItem> makeOrderLineItemsWithoutOrder(final List<MenuQuantityRequest> menuQuantities, final Menus menus) {
        return menuQuantities.stream()
                .map(menuQuantityRequest -> {
                    final Menu menu = menus.findById(menuQuantityRequest.getMenuId());
                    return new OrderLineItem(null, menu, menuQuantityRequest.getQuantity());
                }).collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> {
                    List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(order.getId());
                    return OrderResponse.of(order, OrderLineItemResponse.ofList(orderLineItems));
                }).collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.validateIsNotCompletionOrder();

        final OrderStatus orderStatus = OrderStatus.valueOf(orderStatusRequest.getOrderStatus());
        savedOrder.updateStatus(orderStatus.name());

        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(orderId);

        return OrderResponse.of(savedOrder, OrderLineItemResponse.ofList(orderLineItems));
    }
}
