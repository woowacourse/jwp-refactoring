package kitchenpos.application;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.OrdersRepository;
import kitchenpos.ui.dto.OrderLineItemResponse;
import kitchenpos.ui.dto.OrdersRequest;
import kitchenpos.ui.dto.OrdersResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderTableRepository orderTableRepository;
    private final OrdersRepository ordersRepository;
    private final OrderLineItemService orderLineItemService;

    public OrderService(OrderTableRepository orderTableRepository, OrdersRepository ordersRepository, OrderLineItemService orderLineItemService) {
        this.orderTableRepository = orderTableRepository;
        this.ordersRepository = ordersRepository;
        this.orderLineItemService = orderLineItemService;
    }

    @Transactional
    public OrdersResponse create(final OrdersRequest ordersRequest) {
        List<OrderLineItem> orderLineItems = orderLineItemService.createEntity(ordersRequest);
        OrderTable orderTable = orderTableRepository.findById(ordersRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
        Orders newOrder = ordersRepository.save(new Orders(orderTable, OrderStatus.COOKING, LocalDateTime.now()));
        List<OrderLineItemResponse> orderLineItemResponses = orderLineItemService.saveAll(orderLineItems, newOrder);

        return OrdersResponse.of(newOrder, orderLineItemResponses);
    }

    @Transactional(readOnly = true)
    public List<OrdersResponse> findAll() {
        final List<Orders> allOrders = ordersRepository.findAll();

        List<OrdersResponse> responses = new ArrayList<>();
        for (Orders orders : allOrders) {
            List<OrderLineItemResponse> orderLineItemResponses = orderLineItemService.findAllByOrdersId(orders.getId());
            responses.add(OrdersResponse.of(orders, orderLineItemResponses));
        }

        return responses;
    }

    @Transactional
    public OrdersResponse changeOrderStatus(final Long orderId, final OrdersRequest ordersRequest) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다"));
        order.changeOrderStatus(ordersRequest.getOrderStatus());
        List<OrderLineItemResponse> orderLineItemResponses = orderLineItemService.findAllByOrdersId(orderId);

        return OrdersResponse.of(order, orderLineItemResponses);
    }
}
