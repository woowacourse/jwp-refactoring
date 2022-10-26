package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;

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
            .map(menu -> new OrderLineItem(menu, orderRequest.getQuantity(menu.getId())))
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
