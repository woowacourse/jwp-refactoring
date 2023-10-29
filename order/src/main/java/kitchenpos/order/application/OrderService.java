package kitchenpos.order.application;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.CreateOrderRequest;
import kitchenpos.order.dto.ListOrderResponse;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.order.exception.OrderTableNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderedTableService orderedTableService;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderedTableService orderedTableService) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderedTableService = orderedTableService;
    }

    @Transactional
    public OrderResponse create(final CreateOrderRequest request) {
        final List<Long> menuIds = request.getMenuIds();
        final List<Quantity> quantities = convertToQuantityList(request.getQuantities());
        validateMenusExist(menuIds);
        final OrderLineItems orderLineItems = OrderLineItems.from(menuIds, quantities);

        validateOrderTable(request.getOrderTableId());
        final Order order = Order.of(request.getOrderTableId(), orderLineItems);
        return OrderResponse.of(orderRepository.save(order));
    }

    private void validateMenusExist(final List<Long> menuIds) {
        final List<Menu> allMenus = menuRepository.findAllById(menuIds);
        if (allMenus.size() != menuIds.size()) {
            throw new MenuNotFoundException();
        }
    }

    private List<Quantity> convertToQuantityList(final List<Long> quantities) {
        return quantities.stream()
                .map(Quantity::of)
                .collect(Collectors.toList());
    }

    private void validateOrderTable(final Long orderTableId) {
        if (!orderedTableService.doesTableExist(orderTableId)) {
            throw new OrderTableNotFoundException(orderTableId);
        }
    }

    public ListOrderResponse list() {
        return ListOrderResponse.of(orderRepository.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order savedOrder = findOrder(orderId);
        savedOrder.setOrderStatus(request.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }

    private Order findOrder(final Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
