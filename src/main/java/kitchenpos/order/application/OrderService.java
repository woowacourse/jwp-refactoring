package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderedMenu;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        MenuRepository menuRepository,
        OrderRepository orderRepository,
        OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(OrderRequest orderRequest) {
        List<Menu> menus = menuRepository.findAllById(orderRequest.getMenuIds());
        validateMenuExist(orderRequest.getOrderLineItems(), menus);

        Order order = orderRepository.save(Order.take(
            getOrderTableById(orderRequest.getOrderTableId()),
            createLineItems(orderRequest, menus)
        ));
        return OrderResponse.from(order);
    }

    private void validateMenuExist(List<OrderLineItemRequest> orderLineItemRequests, List<Menu> menus) {
        if (orderLineItemRequests.size() != menus.size()) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 포함되어 있습니다.");
        }
    }

    private OrderTable getOrderTableById(Long tableId) {
        return orderTableRepository.findById(tableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
    }

    private List<OrderLineItem> createLineItems(OrderRequest orderRequest, List<Menu> menus) {
        return menus.stream()
            .map(menu -> new OrderLineItem(
                new OrderedMenu(menu.getName(), menu.getPrice()),
                orderRequest.getQuantity(menu.getId())
            ))
            .collect(Collectors.toList());
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order order = getOrderById(orderId);
        order.updateStatus(orderStatus);
        return OrderResponse.from(order);
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
    }
}
