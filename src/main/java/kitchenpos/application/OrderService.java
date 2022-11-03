package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderMenu;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.request.ChangeOrderStatusRequest;
import kitchenpos.ui.dto.request.OrderCreateRequest;
import kitchenpos.ui.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final MenuGroupRepository menuGroupRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
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
        final List<Long> menuGroupIds = menus.stream()
                .map(Menu::getMenuGroupId)
                .collect(Collectors.toList());
        final List<MenuGroup> menuGroups = menuGroupRepository.findAllById(menuGroupIds);
        return toOrderLineItems(menus, menuGroups, request.getQuantities());
    }

    private List<OrderLineItem> toOrderLineItems(final List<Menu> menus, final List<MenuGroup> menuGroups,
                                                 final List<Long> quantities) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (int i = 0; i < quantities.size(); i++) {
            final Menu menu = menus.get(i);
            final MenuGroup menuGroup = menuGroups.get(i);
            final Long quantity = quantities.get(i);
            final OrderMenu orderMenu = OrderMenu.of(menu, menuGroup);
            final OrderLineItem orderLineItem = new OrderLineItem(quantity, orderMenu);
            orderLineItems.add(orderLineItem);
        }
        return orderLineItems;
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order order = orderRepository.getOrder(orderId)
                .changeOrderStatus(request.getOrderStatus());
        return OrderResponse.from(orderRepository.save(order));
    }
}
