package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemCreateRequest;
import kitchenpos.order.application.dto.OrderUpdateRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderValidator orderValidator;
    private final MenuRepository menuRepository;

    public OrderService(OrderRepository orderRepository, OrderLineItemRepository orderLineItemRepository,
                        OrderValidator orderValidator, MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderValidator = orderValidator;
        this.menuRepository = menuRepository;
    }

    public Order create(final OrderCreateRequest request) {
        List<OrderLineItem> orderLineItems = toOrderLineItems(request.getOrderLineItems());

        Order order = Order.create(request.getOrderTableId(),
                orderLineItems,
                orderValidator);
        return orderRepository.save(order);
    }

    private List<OrderLineItem> toOrderLineItems(List<OrderLineItemCreateRequest> orderLineItemRequests) {
        checkExistMenu(orderLineItemRequests);
        List<OrderLineItem> result = new ArrayList<>();
        Map<Long, Menu> menuMap = mapMenu(orderLineItemRequests);
        for (OrderLineItemCreateRequest request : orderLineItemRequests) {
            Menu menu = findMenu(menuMap, request.getMenuId());
            result.add(new OrderLineItem(null, null, menu.getName(), menu.getPrice(), request.getQuantity()));
        }
        return result;
    }

    private Menu findMenu(Map<Long, Menu> menuMap, Long menuId) {
        try {
            return menuMap.get(menuId);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException();
        }
    }

    private Map<Long, Menu> mapMenu(List<OrderLineItemCreateRequest> orderLineItemRequests) {
        List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());
        List<Menu> foundMenus = menuRepository.findAllById(menuIds);

        return foundMenus.stream()
                .collect(Collectors.toMap(Menu::getId, menu -> menu));
    }

    private void checkExistMenu(final List<OrderLineItemCreateRequest> requests) {
        List<Long> menuIds = requests.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());

        if (requests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    public Order changeOrderStatus(final Long orderId, final OrderUpdateRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.checkUpdatable();

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus);

        orderRepository.save(savedOrder);

        savedOrder.setOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));

        return savedOrder;
    }
}
