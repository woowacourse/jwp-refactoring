package kitchenpos.application.order;

import kitchenpos.domain.common.Quantity;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.dto.order.ChangeOrderStatusRequest;
import kitchenpos.dto.order.CreateOrderRequest;
import kitchenpos.dto.order.ListOrderResponse;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.exception.menu.MenuNotFoundException;
import kitchenpos.exception.order.OrderNotFoundException;
import kitchenpos.exception.order.OrderTableNotFoundException;
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
