package kitchenpos.order.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.model.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.model.Order;
import kitchenpos.order.domain.model.OrderLineItem;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.dto.request.OrderChangeStatusRequest;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.table.domain.model.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
                        OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(final OrderCreateRequest request) {
        OrderTable orderTable = orderTableRepository.findByIdOrThrow(request.getOrderTableId());
        Order order = orderRepository.save(Order.init(orderTable));
        List<OrderLineItem> orderLineItems = createOrderLineItems(order, request.getOrderLineItems());
        order.setUpOrderLineItems(orderLineItems);
        return OrderResponse.from(order);
    }

    private List<OrderLineItem> createOrderLineItems(Order order, List<OrderLineItemRequest> orderLineItemRequests) {
        Map<Long, Menu> menuIdToMenu = findMenus(orderLineItemRequests).stream()
            .collect(Collectors.toMap(Menu::getId, menu -> menu));

        return orderLineItemRequests.stream()
            .map(orderLineItem -> new OrderLineItem(menuIdToMenu.get(orderLineItem.getMenuId()),
                orderLineItem.getQuantity()))
            .collect(Collectors.toList());
    }

    private List<Menu> findMenus(List<OrderLineItemRequest> orderLineRequests) {
        List<Long> menuIds = orderLineRequests.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());
        List<Menu> menus = menuRepository.findAllById(menuIds);
        validateMenusSize(menus, menuIds);
        return menus;
    }

    private void validateMenusSize(List<Menu> menus, List<Long> menuIds) {
        if (menus.size() != menuIds.size()) {
            throw new IllegalArgumentException("올바르지 않은 메뉴입니다.");
        }
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
