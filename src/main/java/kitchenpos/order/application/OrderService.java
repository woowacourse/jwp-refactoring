package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.application.dto.ChangeOrderStatusRequest;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        verifyRequest(request);
        final Order order = orderRepository.save(
                Order.ofCooking(
                        orderTableRepository.getOrderTable(request.getOrderTableId()),
                        LocalDateTime.now(),
                        toOrderLineItems(request)
                )
        );
        return OrderResponse.from(order);
    }

    private void verifyRequest(final OrderCreateRequest request) {
        request.verify();
        final List<Long> menuIds = request.getMenuIds();
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderLineItem> toOrderLineItems(final OrderCreateRequest request) {
        final List<Menu> menus = menuRepository.findAllById(request.getMenuIds());
        return toOrderLineItems(menus, request.getQuantities());
    }

    private List<OrderLineItem> toOrderLineItems(final List<Menu> menus,
                                                 final List<Long> quantities) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (int i = 0; i < quantities.size(); i++) {
            final Menu menu = menus.get(i);
            final Long quantity = quantities.get(i);
            final OrderMenu orderMenu = OrderMenu.from(menu);
            final OrderLineItem orderLineItem = new OrderLineItem(quantity, orderMenu);
            orderLineItems.add(orderLineItem);
        }
        return orderLineItems;
    }

    @Transactional
    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(this::toValidOrder)
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    private Order toValidOrder(final Order order) {
        if (order.isLegacy()) {
            final List<OrderLineItem> orderLineItems = order.getOrderLineItems()
                    .stream()
                    .map(this::withOrderMenu)
                    .collect(Collectors.toList());
            final Order newOrder = order.replaceOrderLineItems(orderLineItems);
            return orderRepository.save(newOrder);
        }
        return order;
    }

    private OrderLineItem withOrderMenu(final OrderLineItem orderLineItem) {
        final Menu menu = menuRepository.findById(orderLineItem.getMenuId())
                .orElseThrow(IllegalArgumentException::new);
        return orderLineItem.addOrderMenu(OrderMenu.from(menu));
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order order = orderRepository.getOrder(orderId)
                .changeOrderStatus(request.getOrderStatus());
        return OrderResponse.from(orderRepository.save(toValidOrder(order)));
    }
}
