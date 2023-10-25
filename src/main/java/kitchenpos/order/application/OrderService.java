package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.OrderLineItemDto;
import kitchenpos.order.application.request.OrderCreateRequest;
import kitchenpos.order.application.request.OrderStatusUpdateRequest;
import kitchenpos.order.application.response.OrderResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

    @Transactional
    public OrderResponse create(OrderCreateRequest orderCreateRequest) {
        List<OrderLineItemDto> orderLineItemDtos = orderCreateRequest.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new IllegalArgumentException("주문의 메뉴가 존재하지 않습니다.");
        }

        OrderTable orderTable = orderTableRepository.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("EMPTY 상태인 테이블에 주문할 수 없습니다.");
        }

        Order order = Order.builder()
                .orderTable(orderTable)
                .orderStatus(OrderStatus.COOKING)
                .build();

        List<OrderLineItem> orderLineItems = orderLineItemDtos.stream()
                .map(orderLineItemDto -> getOrderLineItem(orderLineItemDto, order))
                .collect(Collectors.toList());

        order.addOrderLineItems(orderLineItems);
        Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    private OrderLineItem getOrderLineItem(OrderLineItemDto orderLineItemDto, Order order) {
        Menu menu = findMenuById(orderLineItemDto.getMenuId());
        return OrderLineItem.builder()
                .order(order)
                .menu(menu)
                .quantity(orderLineItemDto.getQuantity())
                .build();
    }

    private Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 메뉴를 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatusUpdateRequest orderStatusUpdateRequest) {
        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 주문을 찾을 수 없습니다."));

        if (foundOrder.isCompleted()) {
            throw new IllegalArgumentException("완료된 주문의 상태를 변경할 수 없습니다.");
        }

        String orderStatusRequest = orderStatusUpdateRequest.getOrderStatus();
        OrderStatus orderStatus = OrderStatus.from(orderStatusRequest);
        foundOrder.updateStatus(orderStatus);
        return OrderResponse.from(foundOrder);
    }
}
