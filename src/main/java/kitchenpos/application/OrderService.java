package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.collection.OrderLineItems;
import kitchenpos.domain.collection.OrderTables;
import kitchenpos.domain.collection.Orders;
import kitchenpos.domain.entity.Order;
import kitchenpos.domain.entity.OrderLineItem;
import kitchenpos.domain.entity.OrderStatus;
import kitchenpos.domain.entity.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.ui.jpa.dto.order.ChangeOrderStatusResponse;
import kitchenpos.ui.jpa.dto.order.OrderCreateRequest;
import kitchenpos.ui.jpa.dto.order.OrderCreateResponse;
import kitchenpos.ui.jpa.dto.order.OrderListResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private MenuRepository menuRepository;
    private TableServiceAssistant tableServiceAssistant;
    private OrderLineItemService orderLineItemService;

    public OrderService(OrderRepository orderRepository, MenuRepository menuRepository,
                        TableServiceAssistant tableServiceAssistant,
                        OrderLineItemService orderLineItemService) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.tableServiceAssistant = tableServiceAssistant;
        this.orderLineItemService = orderLineItemService;
    }

    @Transactional
    public OrderCreateResponse create(OrderCreateRequest orderCreateRequest) {
        OrderLineItems orderLineItems = new OrderLineItems(orderCreateRequest.getOrderLineItems());

        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException();
        }

        List<Long> menuIds = orderLineItems.getMenuIds();

        if (menuIds.size() != menuRepository.countExistMenu(menuIds)) {
            throw new IllegalArgumentException();
        }

        OrderTable orderTable = tableServiceAssistant.findTable(orderCreateRequest.getOrderTableId());

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Order order = new Order(orderTable, orderLineItems.getElements());
        orderRepository.save(order);

        orderLineItems.associateOrder(order);
        orderLineItemService.saveOrderLineItems(orderLineItems);

        return new OrderCreateResponse(order.getId(), orderTable.getId(), order.getOrderStatus().getValue(),
                order.getOrderedTime(), orderLineItems.getOrderLineItemIds());
    }

    public List<OrderListResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> new OrderListResponse(order.getId(), order.getOrderTable().getId(),
                        order.getOrderStatus().getValue(), order.getOrderedTime(), new OrderLineItems(order.getOrderLineItems()).getOrderLineItemIds()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ChangeOrderStatusResponse changeOrderStatus(Long orderId, String orderStatus) {
        Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(OrderStatus.valueOf(orderStatus));

        return new ChangeOrderStatusResponse(savedOrder.getId(), savedOrder.getOrderTable().getId(),
                savedOrder.getOrderStatus().getValue(), savedOrder.getOrderedTime(), new OrderLineItems(savedOrder.getOrderLineItems()).getOrderLineItemIds());
    }

    public boolean isNotAllOrderFinish(OrderTable orderTable) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), List.of(OrderStatus.COOKING.getValue(), OrderStatus.MEAL.getValue()));
    }

    public Orders findOrdersInOrderTables(OrderTables orderTables) {
        return new Orders(orderRepository.findAllByOrderTableIdIn(orderTables.getOrderTableIds()));
    }
}
