package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
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
        OrderTable orderTable = orderTableService.findById(orderRequest.getOrderTableId());

        Order order = new Order(orderTable, orderRequest.getOrderStatus());

        Order savedOrder = orderRepository.save(order);

        List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItemRequests();
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu menu = menuService.findById(orderLineItemRequest.getMenuId());
            OrderLineItem orderLineItem = new OrderLineItem(savedOrder, menu, orderLineItemRequest.getQuantity());
            orderLineItems.add(orderLineItem);

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
