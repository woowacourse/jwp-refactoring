package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final MenuRepository menuRepository;

    public OrderService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, OrderLineItemRepository orderLineItemRepository, MenuRepository menuRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.menuRepository = menuRepository;
    }

    public OrderResponse create(final OrderRequest request) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest data : request.getOrderLineItemRequests()) {
            if (!menuRepository.existsById(data.getMenuId())) {
                throw new IllegalArgumentException();
            }
            OrderLineItem saved = orderLineItemRepository.save(new OrderLineItem(data.getSeq(), data.getMenuId(), data.getQuantity()));
            orderLineItems.add(saved);
        }
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId()).orElseThrow(IllegalArgumentException::new);
        Order savedOrder = new Order(orderTable, orderLineItems, OrderStatus.COOKING, LocalDateTime.now());
        orderRepository.save(savedOrder);
        return OrderResponse.of(savedOrder);
    }

    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return OrderResponse.listOf(orders);
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        Order savedOrder = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
        System.out.println(savedOrder.getOrderStatus());
        savedOrder.changeStatus(request.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }
}
