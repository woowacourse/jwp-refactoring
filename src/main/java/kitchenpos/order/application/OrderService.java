package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.menu.application.MenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineItemService orderLineItemService;
    private final MenuService menuService;
    private final OrderTableService orderTableService;

    public OrderService(
        final OrderRepository orderRepository,
        final OrderLineItemService orderLineItemService,
        final MenuService menuService,
        final OrderTableService orderTableService
    ) {
        this.orderRepository = orderRepository;
        this.orderLineItemService = orderLineItemService;
        this.menuService = menuService;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        orderTableService.check(orderRequest.getOrderTableId());

        Order order = new Order(orderRequest.getOrderTableId(), orderRequest.getOrderStatus());

        Order savedOrder = orderRepository.save(order);

        List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItemRequests();
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            orderLineItems.add(
                new OrderLineItem(savedOrder,
                    orderLineItemRequest.getMenuId(), orderLineItemRequest.getQuantity()));
        }

        menuService.checkCount(orderLineItems);

        orderLineItemService.saveAll(orderLineItems);

        return savedOrder;
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }


    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(() ->  new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());

        return savedOrder;
    }
}
