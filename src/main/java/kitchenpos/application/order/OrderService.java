package kitchenpos.application.order;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.order.request.OrderRequest;
import kitchenpos.dto.order.response.OrderResponse;
import kitchenpos.repository.order.OrderRepository;
import kitchenpos.repository.order.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemService orderLineItemService;

    public OrderService(OrderRepository orderRepository, OrderTableRepository orderTableRepository,
                        OrderLineItemService orderLineItemService) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemService = orderLineItemService;
    }

    @Transactional
    public OrderResponse create(OrderRequest orderRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 order table id 입니다."));
        Order savedOrder = orderRepository.save(Order.cooking(orderTable));
        List<OrderLineItem> orderLineItems = orderLineItemService.createOrderLineItems(
                savedOrder, orderRequest.getOrderLineItemRequests());
        return OrderResponse.of(savedOrder, orderLineItems);
    }

    public List<OrderResponse> list() {
        OrderLineItems orderLineItems = orderLineItemService.findAll();
        return orderLineItems.createOrderResponses();
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, Order order) {
        Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 order id 입니다."));
        return savedOrder.changeOrderStatus(order.getOrderStatus());
    }
}
