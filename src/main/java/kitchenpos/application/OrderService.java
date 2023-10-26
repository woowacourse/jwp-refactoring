package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
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

    public Long create(final OrderCreateRequest request) {
        final List<Menu> menus = menuRepository.findByIdIn(request.extractMenuIds());
        final Map<Menu, Long> menuWithQuantityMap = makeMenuWithQuantityMap(menus, request.extractMenuIdWithQuantityMap());
        final OrderTable orderTable = orderTableRepository.getById(request.getOrderTableId());
        orderTable.validateIsEmpty();
        final Order order = Order.ofCooking(orderTable, menuWithQuantityMap);
        return orderRepository.save(order).getId();
    }

    private Map<Menu, Long> makeMenuWithQuantityMap(final List<Menu> menus, final Map<Long, Long> menuIdWithQuantityMap) {
        validateAllMenusFound(menus, menuIdWithQuantityMap);
        return menus.stream()
                .collect(Collectors.toMap(
                        menu -> menu,
                        menu -> menuIdWithQuantityMap.get(menu.getId())
                ));
    }

    private static void validateAllMenusFound(final List<Menu> menus, final Map<Long, Long> menuIdWithQuantityMap) {
        final boolean isAllMatch = menus.stream().allMatch(menu -> menuIdWithQuantityMap.containsKey(menu.getId()));
        if (isAllMatch) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        final List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final String orderStatusName) {
        final Order savedOrder = orderRepository.getById(orderId);
        final OrderStatus orderStatus = OrderStatus.from(orderStatusName);
        savedOrder.updateOrderStatus(orderStatus);
        return OrderResponse.from(savedOrder);
    }
}
