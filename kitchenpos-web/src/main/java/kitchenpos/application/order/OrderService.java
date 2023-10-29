package kitchenpos.application.order;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.*;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.exception.KitchenposException;
import kitchenpos.presentation.order.dto.OrderLineItemRequest;
import kitchenpos.presentation.order.dto.OrderRequest;
import kitchenpos.presentation.order.dto.UpdateOrderStateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static kitchenpos.exception.ExceptionInformation.*;


@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository, final OrderLineItemRepository orderLineItemRepository, final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        validateOrderItemsSize(orderRequest);

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new KitchenposException(ORDER_TABLE_NOT_FOUND));

        final Order order = Order.create(orderTable);
        final Order savedOrder = orderRepository.save(order);

        final List<Long> menuIds = orderRequest.getOrderLineItems()
                .stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
        final List<Menu> menusInOrder = menuRepository.findByIds(menuIds);

        final List<OrderLineItem> orderLineItems = getOrderItems(orderRequest.getOrderLineItems(), menusInOrder, savedOrder);
        final List<OrderLineItem> savedOrderLineItems = orderLineItemRepository.saveAll(orderLineItems);
        savedOrder.updateOrderLineItems(savedOrderLineItems);

        return savedOrder;
    }

    private void validateOrderItemsSize(final OrderRequest orderRequest) {
        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItems())) {
            throw new KitchenposException(ORDER_LINE_ITEMS_IS_EMPTY);
        }
    }

    private List<OrderLineItem> getOrderItems(final List<OrderLineItemRequest> orderLineItems, final List<Menu> menusInOrder, final Order order) {
        validateAllMenuExist(orderLineItems, menusInOrder);
        return makeOrderItems(orderLineItems, menusInOrder, order);
    }

    private void validateAllMenuExist(final List<OrderLineItemRequest> orderLineItems, final List<Menu> menusInOrder) {
        if (orderLineItems.size() != menusInOrder.size()) {
            throw new KitchenposException(ORDER_ITEM_NOT_FOUND_OR_DUPLICATE);
        }
    }

    private List<OrderLineItem> makeOrderItems(final List<OrderLineItemRequest> orderLineItems, final List<Menu> menusInOrder, final Order order) {
        final Map<Long, Menu> menuMap = menusInOrder.stream()
                .collect(Collectors.toUnmodifiableMap(Menu::getId, Function.identity()));

        return orderLineItems.stream()
                .map(orderLineItemRequest -> {
                    final Menu menu = menuMap.get(orderLineItemRequest.getMenuId());
                    return OrderLineItem.create(menu.getName(), menu.getPrice(), orderLineItemRequest.getQuantity(), order);
                })
                .collect(Collectors.toList());
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final UpdateOrderStateRequest updateOrderStateRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(updateOrderStateRequest.getOrderState());
        savedOrder.updateOrderStatus(orderStatus);
        return savedOrder;
    }
}
