package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateOrderDto;
import kitchenpos.application.dto.CreateOrderLineItemDto;
import kitchenpos.application.dto.OrderDto;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItemRepository;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public OrderDto create(final CreateOrderDto createOrderDto) {
        final Order order = Order.create(
                createOrderDto.getOrderTableId(),
                mapToOrderLineItems(createOrderDto)
        );
        validateExistMenus(createOrderDto.getMenuIds(), order.getOrderLineItems());
        validateTableFull(order);

        final Order savedOrder = orderRepository.save(order);

        return OrderDto.of(savedOrder);
    }

    private List<OrderLineItem> mapToOrderLineItems(final CreateOrderDto createOrderDto) {
        final List<CreateOrderLineItemDto> createOrderLineItemDtos = createOrderDto.getOrderLineItems();
        final List<Long> menuIds = createOrderDto.getMenuIds();
        final List<Menu> menus = menuRepository.findAllByIdsIn(menuIds);

        return createOrderLineItemDtos.stream()
                .map(it -> {
                    final Menu menu = findMenuById(menus, it.getMenuId());
                    return new OrderLineItem(menu.getName(), menu.getPrice(), it.getMenuId(), it.getQuantity());
                })
                .collect(Collectors.toList());
    }

    private Menu findMenuById(final List<Menu> menus, final Long menuId) {
        return menus.stream()
                .filter(menu -> menu.getId().equals(menuId))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateExistMenus(final List<Long> menuIds, final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateTableFull(final Order order) {
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderDto> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(orderStatus);

        return OrderDto.of(orderRepository.save(savedOrder));
    }
}
