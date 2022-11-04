package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.collection.OrderLineItems;
import kitchenpos.order.domain.collection.Orders;
import kitchenpos.order.domain.entity.Order;
import kitchenpos.order.domain.entity.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.ui.dto.order.ChangeOrderStatusResponse;
import kitchenpos.order.ui.dto.order.OrderCreateRequest;
import kitchenpos.order.ui.dto.order.OrderCreateResponse;
import kitchenpos.order.ui.dto.order.OrderListResponse;
import kitchenpos.table.application.TableServiceAssistant;
import kitchenpos.table.domain.collection.OrderTables;
import kitchenpos.table.domain.entity.OrderTable;
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

        Order order = new Order(orderTable);
        orderRepository.save(order);

        orderLineItems.associateOrder(order);
        orderLineItemService.saveOrderLineItems(orderLineItems);

        return new OrderCreateResponse(order.getId(), orderTable.getId(), order.getOrderStatus().getValue(),
                order.getOrderedTime(), orderLineItems.getOrderLineItemIds());
    }

    public List<OrderListResponse> list() {
        List<OrderListResponse> orderListResponses = new ArrayList<>();
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            OrderLineItems orderLineItems = orderLineItemService.findOrderLineItemsInOrder(order.getId());
            OrderListResponse orderListResponse = new OrderListResponse(order.getId(), order.getOrderTable().getId(),
                    order.getOrderStatus().getValue(), order.getOrderedTime(), orderLineItems.getOrderLineItemIds());
            orderListResponses.add(orderListResponse);
        }
        return orderListResponses;
    }

    @Transactional
    public ChangeOrderStatusResponse changeOrderStatus(Long orderId, String orderStatus) {
        Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(OrderStatus.valueOf(orderStatus));
        OrderLineItems orderLineItems = orderLineItemService.findOrderLineItemsInOrder(savedOrder.getId());

        return new ChangeOrderStatusResponse(savedOrder.getId(), savedOrder.getOrderTable().getId(),
                savedOrder.getOrderStatus().getValue(), savedOrder.getOrderedTime(), orderLineItems.getOrderLineItemIds());
    }

    public boolean isNotAllOrderFinish(OrderTable orderTable) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), List.of(OrderStatus.COOKING.getValue(), OrderStatus.MEAL.getValue()));
    }

    public Orders findOrdersInOrderTables(OrderTables orderTables) {
        return new Orders(orderRepository.findAllByOrderTableIdIn(orderTables.getOrderTableIds()));
    }
}
