package kitchenpos.application.order;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderCreationRequest;
import kitchenpos.application.dto.OrderItemsWithQuantityRequest;
import kitchenpos.application.dto.OrderStatusChangeRequest;
import kitchenpos.application.dto.result.OrderResult;
import kitchenpos.dao.menu.MenuRepository;
import kitchenpos.dao.order.OrderLineItemRepository;
import kitchenpos.dao.order.OrderRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderValidator orderValidator;
    private final MenuRepository menuRepository;

    public OrderService(
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderValidator orderValidator,
            final MenuRepository menuRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderValidator = orderValidator;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResult create(final OrderCreationRequest request) {
        final Long orderTableId = request.getOrderTableId();
        final Order order = orderRepository.save(new Order(orderTableId, orderValidator));
        final List<OrderLineItem> orderLineItems = getOrderLineItemsByRequest(order, request.getOrderLineItems());
        orderLineItemRepository.saveAll(orderLineItems);
        return OrderResult.from(order);
    }

    private List<OrderLineItem> getOrderLineItemsByRequest(
            final Order order,
            final List<OrderItemsWithQuantityRequest> orderLineItemRequests
    ) {
        final List<Long> menuIds = extractMenuIds(orderLineItemRequests);
        final Map<Long, Menu> menusByMenuId = menuRepository.findAllByIdIn(menuIds).stream().collect(Collectors.toMap(
                Menu::getId, Function.identity()
        ));
        validateExistMenus(menuIds, menusByMenuId.values());
        return orderLineItemRequests.stream().map(request -> {
            final Menu menu = menusByMenuId.get(request.getMenuId());
            return new OrderLineItem(order, menu.getId(), menu.getName(), menu.getPrice(), request.getQuantity());
        }).collect(Collectors.toList());
    }

    public void validateExistMenus(final List<Long> menuIds, final Collection<Menu> menus) {
        if (menus.isEmpty() || !Objects.equals(menus.size(), menuIds.size())) {
            throw new IllegalArgumentException("Menu does not exist.");
        }
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
