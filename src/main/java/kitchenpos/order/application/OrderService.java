package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.request.OrderCreateRequest;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final MenuService menuService;
    private final OrderLineItemService orderLineItemService;
    private final TableService tableService;

    public OrderService(
            OrderRepository orderRepository,
            MenuService menuService,
            OrderLineItemService orderLineItemService,
            TableService tableService
    ) {
        this.orderRepository = orderRepository;
        this.menuService = menuService;
        this.orderLineItemService = orderLineItemService;
        this.tableService = tableService;
    }

    @Transactional
    public Order create(OrderCreateRequest request) {
        OrderTable orderTable = tableService.getById(request.getOrderTableId());
        Order savedOrder = orderRepository.save(Order.create(orderTable));
        List<OrderLineItem> orderLineItems = orderLineItemService.create(savedOrder,
                request.getOrderLineItems());
        validateOrderLineItemsSize(orderLineItems);
        return savedOrder;
    }

    private void validateOrderLineItemsSize(List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
                                           .map(OrderLineItem::getMenuId)
                                           .collect(Collectors.toList());

        if (orderLineItems.size() != menuService.findAllByIds(menuIds).size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order savedOrder = orderRepository.findById(orderId)
                                          .orElseThrow(IllegalArgumentException::new);
        savedOrder.changeOrderStatus(orderStatus);
        return savedOrder;
    }
}
