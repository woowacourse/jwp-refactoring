package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.request.OrderChangeStatusRequest;
import kitchenpos.ui.dto.request.OrderCreateRequest;
import kitchenpos.ui.dto.request.OrderLineItemCreateRequest;
import kitchenpos.ui.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
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
    public OrderResponse create(final OrderCreateRequest orderInput) {
        final OrderTable orderTable = orderTableRepository.findMandatoryById(orderInput.getOrderTableId());
        validateOrderTableOccupied(orderTable);
        final Order order = new Order(OrderStatus.COOKING, orderTable);
        final List<OrderLineItem> orderLineItems = createOrderLineItems(orderInput.getOrderLineItems(), order);
        order.addOrderLineItems(orderLineItems);
        return OrderResponse.from(orderRepository.save(order));
    }

    private void validateOrderTableOccupied(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderLineItem> createOrderLineItems(
            List<OrderLineItemCreateRequest> orderLineItemsInput,
            final Order order
    ) {
        validateOrderLineItemsInputNotEmpty(orderLineItemsInput);
        final List<Long> menuIds = extractMenuIds(orderLineItemsInput);
        final Map<Long, Menu> menus = menuRepository.findAllById(menuIds).stream()
                .collect(Collectors.toMap(Menu::getId, Function.identity()));
        validateMenuIds(menuIds, menus);
        return orderLineItemsInput.stream()
                .map(menuIdAndQuantity -> new OrderLineItem(
                        menuIdAndQuantity.getQuantity(),
                        order,
                        menus.get(menuIdAndQuantity.getMenuId())))
                .collect(Collectors.toUnmodifiableList());
    }

    private void validateOrderLineItemsInputNotEmpty(final List<OrderLineItemCreateRequest> orderLineItemsInput) {
        if (CollectionUtils.isEmpty(orderLineItemsInput)) {
            throw new IllegalArgumentException();
        }
    }

    private List<Long> extractMenuIds(final List<OrderLineItemCreateRequest> orderLineItemsInput) {
        return orderLineItemsInput.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toUnmodifiableList());
    }

    private void validateMenuIds(final List<Long> menuIds, final Map<Long, Menu> menus) {
        if (menuIds.size() != menus.size()) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderResponse> list() {
        return orderRepository.joinAllOrderLineItems().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderChangeStatusRequest orderStatusInput) {
        final Order order = orderRepository.joinOrderLineItemsMandatoryById(orderId);
        final OrderStatus orderStatus = orderStatusInput.getOrderStatus();
        validateOrderStatusNotCompletion(order.getOrderStatus());
        order.changeStatus(orderStatus);
        return OrderResponse.from(order);
    }

    private void validateOrderStatusNotCompletion(final OrderStatus orderStatus) {
        if (orderStatus.isCompletion()) {
            throw new IllegalArgumentException();
        }
    }
}
