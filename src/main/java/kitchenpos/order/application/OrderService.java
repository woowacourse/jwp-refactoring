package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.domain.vo.OrderStatus;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.request.OrderUpdateStatusRequest;
import kitchenpos.order.dto.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Transactional
@Service
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

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse create(OrderCreateRequest orderCreateRequest) {
        List<OrderLineItemRequest> orderLineItemRequests = orderCreateRequest.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException("주문의 메뉴가 존재하지 않습니다.");
        }

        OrderTable orderTable = orderTableRepository.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("테이블 정보가 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블에 주문을 등록할 수 없습니다.");
        }

        Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now());
        orderRepository.save(order);
        List<OrderLineItem> orderLineItems = createMenusForOrder(order, orderCreateRequest.getOrderLineItems());
        order.addMenus(orderLineItems);
        return OrderResponse.from(order);
    }

    private List<OrderLineItem> createMenusForOrder(Order order, List<OrderLineItemRequest> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> createOrderLineItem(order, orderLineItem))
                .collect(Collectors.toList());
    }

    private OrderLineItem createOrderLineItem(Order order, OrderLineItemRequest orderLineItem) {
        Menu menu = menuRepository.findById(orderLineItem.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 메뉴입니다."));
        return new OrderLineItem(order, menu, orderLineItem.getQuantity());
    }

    public OrderResponse changeOrderStatus(Long orderId, OrderUpdateStatusRequest orderRequest) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        order.checkEditable();
        OrderStatus orderStatus = OrderStatus.from(orderRequest.getOrderStatus());
        order.updateStatus(orderStatus);
        return OrderResponse.from(order);
    }
}
