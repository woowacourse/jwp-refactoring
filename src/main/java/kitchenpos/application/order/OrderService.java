package kitchenpos.application.order;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderCreationRequest;
import kitchenpos.application.dto.OrderItemsWithQuantityRequest;
import kitchenpos.application.dto.OrderStatusChangeRequest;
import kitchenpos.application.dto.result.OrderResult;
import kitchenpos.dao.menu.MenuRepository;
import kitchenpos.dao.order.OrderRepository;
import kitchenpos.dao.table.OrderTableRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.table.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final MenuRepository menuRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResult create(final OrderCreationRequest request) {
        final List<OrderItemsWithQuantityRequest> orderLineItemRequests = request.getOrderLineItems();
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("Order table does not exist."));
        final Order order = new Order(orderTable);
        final List<OrderLineItem> orderLineItems = getOrderLineItemsByRequest(order, orderLineItemRequests);
        order.applyOrderLineItems(orderLineItems);
        return OrderResult.from(orderRepository.save(order));
    }

    private List<OrderLineItem> getOrderLineItemsByRequest(
            final Order order,
            final List<OrderItemsWithQuantityRequest> orderLineItemRequests
    ) {
        final List<Long> menuIds = extractMenuIds(orderLineItemRequests);
        final Map<Long, Menu> menusById = menuRepository.findAllByIdIn(menuIds).stream()
                .collect(Collectors.toMap(Menu::getId, Function.identity()));
        return orderLineItemRequests.stream().map(orderItemRequest -> {
            final Menu menu = getMenuByRequestId(orderItemRequest, menusById);
            return new OrderLineItem(order, menu, orderItemRequest.getQuantity());
        }).collect(Collectors.toList());
    }

    private Menu getMenuByRequestId(
            final OrderItemsWithQuantityRequest orderItemRequest,
            final Map<Long, Menu> menusById
    ) {
        return menusById.computeIfAbsent(orderItemRequest.getMenuId(), id -> {
            throw new IllegalArgumentException("Menu does not exist.");
        });
    }

    private List<Long> extractMenuIds(final List<OrderItemsWithQuantityRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(OrderItemsWithQuantityRequest::getMenuId)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResult> list() {
        return orderRepository.findAll().stream()
                .map(OrderResult::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResult changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order existOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        existOrder.changeOrderStatus(request.getOrderStatus());
        return OrderResult.from(orderRepository.save(existOrder));
    }
}
