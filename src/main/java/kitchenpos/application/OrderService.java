package kitchenpos.application;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItemValidator;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.domain.order.OrderStatus.valueOf;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemValidator orderLineItemValidator;

    public OrderService(OrderRepository orderRepository,
                        OrderTableRepository orderTableRepository,
                        OrderLineItemValidator orderLineItemValidator) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemValidator = orderLineItemValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {

        orderLineItemValidator.validate(orderRequest.getOrderLineItems());

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다. 주문을 등록할 수 없습니다."));
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다. 주문을 등록할 수 없습니다.");
        }

        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems();
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalStateException("주문 항목이 존재하지 않습니다. 주문을 등록할 수 없습니다.");
        }

        Order order = new Order(null, orderTable, valueOf(orderRequest.getOrderStatus()),
                orderRequest.getOrderedTime());
        order.addOrderLineItems(orderLineItems);
        final Order savedOrder = orderRepository.save(order);

        return OrderResponse.from(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다. 주문상태를 변경할 수 없습니다."));

        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문테이블이 존재하지 않습니다. 주문상태를 변경할 수 없습니다."));

        Order requestOrder = new Order(orderTable, request.getOrderStatus(), request.getOrderedTime());

        savedOrder.updateStatus(requestOrder);
        return OrderResponse.from(savedOrder);
    }
}
