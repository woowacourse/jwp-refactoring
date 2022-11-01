package kitchenpos.application;

import kitchenpos.application.dto.OrderLineItemRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {
        final OrderLineItems orderLineItems = createOrderLineItemsByRequest(orderRequest);

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        final Order order = orderRepository.save(new Order(null, orderTable, orderRequest.getOrderStatus(),
                orderRequest.getOrderedTime(), orderLineItems.getOrderLineItems()));

        return new Order(order.getId(), order.getOrderTable(), order.getOrderStatus(),
                order.getOrderedTime(), createOrderLineItems(orderLineItems, order).getOrderLineItems());
    }

    private OrderLineItems createOrderLineItems(OrderLineItems orderLineItems, Order order) {
        return new OrderLineItems(orderLineItems.getOrderLineItems()
                .stream()
                .map(orderLineItem -> orderLineItemRepository.save(new OrderLineItem(order,
                        orderLineItem.getMenuId(), orderLineItem.getQuantity())))
                .collect(Collectors.toList()));
    }

    private OrderLineItems createOrderLineItemsByRequest(OrderRequest orderRequest) {
        final OrderLineItems orderLineItems = new OrderLineItems(orderRequest.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItem(null, it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList()));

        final List<Long> menuIds = mapToMenuIds(orderRequest);
        if (!orderLineItems.isSameMenuSize(menuRepository.countByIdIn(menuIds))) {
            throw new IllegalArgumentException();
        }
        return orderLineItems;
    }

    private List<Long> mapToMenuIds(OrderRequest orderRequest) {
        return orderRequest.getOrderLineItems()
                .stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.validateStatusForChange();

        return orderRepository.save(new Order(savedOrder.getId(), savedOrder.getOrderTable(), order.getOrderStatus(),
                savedOrder.getOrderedTime(),
                new OrderLineItems(orderLineItemRepository.findAllByOrderId(orderId)).getOrderLineItems()));
    }
}
