package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.OrderLineItemDto;
import kitchenpos.order.application.request.OrderCreateRequest;
import kitchenpos.order.application.request.OrderStatusUpdateRequest;
import kitchenpos.order.application.response.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository,
            OrderValidator orderValidator
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderValidator = orderValidator;
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

        List<OrderLineItem> orderLineItems = convertToOrderLineItems(orderCreateRequest.getOrderLineItems());

        Order order = Order.builder()
                .orderTable(orderTable)
                .orderLineItems(orderLineItems)
                .orderStatus(OrderStatus.COOKING)
                .build();
        orderValidator.validate(order);

        Order savedOrder = orderRepository.save(order);
        return OrderResponse.from(savedOrder);
    }

    private List<OrderLineItem> convertToOrderLineItems(List<OrderLineItemDto> orderLineItemDtos) {
        return orderLineItemDtos.stream()
                .map(this::createOrderLineItem)
                .collect(Collectors.toList());
    }

    private OrderLineItem createOrderLineItem(OrderLineItemDto orderLineItemDto) {
        return OrderLineItem.builder()
                .menuId(orderLineItemDto.getMenuId())
                .quantity(orderLineItemDto.getQuantity())
                .build();
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
