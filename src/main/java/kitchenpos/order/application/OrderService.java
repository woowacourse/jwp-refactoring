package kitchenpos.order.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.model.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.model.MenuSnapShot;
import kitchenpos.order.domain.model.Order;
import kitchenpos.order.domain.model.OrderLineItem;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.service.OrderValidator;
import kitchenpos.order.dto.request.OrderChangeStatusRequest;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderValidator orderValidator;

    public OrderService(OrderRepository orderRepository, MenuRepository menuRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderValidator = orderValidator;
    }

    public OrderResponse create(OrderCreateRequest request) {
        List<OrderLineItem> orderLineItems = createOrderLineItems(request.getOrderLineItems());
        Order order = orderRepository.save(Order.create(request.getOrderTableId(), orderLineItems, orderValidator));
        return OrderResponse.from(order);
    }

    private List<OrderLineItem> createOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests) {
        Map<Long, Menu> menuIdToMenu = findMenus(orderLineItemRequests).stream()
            .collect(Collectors.toMap(Menu::getId, menu -> menu));

        return orderLineItemRequests.stream()
            .map(orderLineItem -> {
                Menu menu = menuIdToMenu.get(orderLineItem.getMenuId());
                MenuSnapShot menuSnapShot = new MenuSnapShot(menu.getName(), menu.getPrice());
                return new OrderLineItem(orderLineItem.getMenuId(), orderLineItem.getQuantity(), menuSnapShot);
            })
            .collect(Collectors.toList());
    }


    private List<Menu> findMenus(List<OrderLineItemRequest> orderLineRequests) {
        List<Long> menuIds = orderLineRequests.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());
        return menuRepository.findAllById(menuIds);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAllWithFetch().stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(Long orderId, OrderChangeStatusRequest request) {
        Order order = orderRepository.findByIdOrThrow(orderId);
        order.changeStatus(request.getOrderStatus());
        return OrderResponse.from(order);
    }
}
