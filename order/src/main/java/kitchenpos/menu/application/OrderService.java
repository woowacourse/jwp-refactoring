package kitchenpos.menu.application;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Order;
import kitchenpos.menu.domain.OrderLineItem;
import kitchenpos.menu.domain.OrderLineItemRepository;
import kitchenpos.menu.domain.OrderMenu;
import kitchenpos.menu.domain.OrderRepository;
import kitchenpos.menu.domain.OrderStatus;
import kitchenpos.menu.dto.OrderRequest;
import kitchenpos.menu.dto.OrderResponse;
import kitchenpos.menu.dto.OrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderLineItemRepository orderLineItemRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = new Order(orderRequest.getOrderTableId());

        Order savedOrder = orderRepository.save(order);
        List<OrderLineItem> orderLineItems = orderLineItemRepository.saveAll(
            newOrderLineItems(savedOrder, orderRequest)
        );

        return OrderResponse.from(savedOrder, orderLineItems);
    }

    private List<OrderLineItem> newOrderLineItems(final Order order,
                                                  final OrderRequest orderRequest) {
        if (Objects.isNull(orderRequest.getOrderLineItems())) {
            throw new IllegalArgumentException();
        }

        Map<Long, Long> menuIdAndQuantity = orderRequest.toMap();
        List<Menu> menus = menuRepository.findAllById(menuIdAndQuantity.keySet());
        validateMenusSize(orderRequest, menus);

        return orderLineItemsFromMenus(order, menuIdAndQuantity, menus);
    }

    private List<OrderLineItem> orderLineItemsFromMenus(final Order order,
                                                        final Map<Long, Long> menuIdAndQuantity,
                                                        final List<Menu> menus) {
        return menus.stream()
            .map(menu -> new OrderLineItem(order,
                new OrderMenu(menu.getId(), menu.getName(), menu.getPrice()),
                menuIdAndQuantity.get(menu.getId())))
            .collect(Collectors.toList());
    }

    private void validateMenusSize(final OrderRequest orderRequest, final List<Menu> menus) {
        if (menus.isEmpty() || orderRequest.getOrderLineItems().size() != menus.size()) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderResponse> list() {
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAll();
        return OrderResponse.listFrom(orderLineItems);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
                                           final OrderStatusRequest orderStatusRequest) {
        Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);
        OrderStatus orderStatus = OrderStatus.valueOf(orderStatusRequest.getOrderStatus());
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrder(savedOrder);

        savedOrder.changeOrder(orderStatus);
        return OrderResponse.from(savedOrder, orderLineItems);
    }
}
