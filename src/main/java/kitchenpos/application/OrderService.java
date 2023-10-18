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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemValidator orderLineItemValidator;

    public OrderService(final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository,
                        final OrderLineItemValidator orderLineItemValidator) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemValidator = orderLineItemValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다. 주문을 등록할 수 없습니다."));
        orderLineItemValidator.validateOrderMenuExist(orderRequest.getOrderLineItems());

        final Order order = new Order(orderTable, orderRequest.getOrderStatus(), orderRequest.getOrderedTime());
        final List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems();
        order.addOrderLineItems(orderLineItems);
        orderRepository.save(order);

        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다. 주문상태를 변경할 수 없습니다."));
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문테이블이 존재하지 않습니다. 주문상태를 변경할 수 없습니다."));

        final Order changeOrder = new Order(orderTable, request.getOrderStatus(), request.getOrderedTime());
        order.updateStatus(changeOrder);

        return OrderResponse.from(order);
    }
}
