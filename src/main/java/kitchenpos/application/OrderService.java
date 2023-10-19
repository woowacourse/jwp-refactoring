package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.vo.Quantity;
import kitchenpos.dto.OrderCreateOrderLineItemRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
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

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderLineItemRepository orderLineItemRepository,
                        final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderCreateRequest request) {
        validateOrderLineItemsOrderable(request);

        final OrderTable findOrderTable = orderTableRepository.findOrderTableById(request.getOrderTableId());
        final Order newOrder = Order.ofEmptyOrderLineItems(findOrderTable);
        newOrder.addOrderLineItems(createOrderLineItems(request));

        return orderRepository.save(newOrder);
    }

    private void validateOrderLineItemsOrderable(final OrderCreateRequest request) {
        final List<Long> requestMenuIds = request.getOrderLineItems()
                .stream()
                .map(OrderCreateOrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (request.getOrderLineItems().size() != menuRepository.countByIdIn(requestMenuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderLineItem> createOrderLineItems(final OrderCreateRequest request) {
        return request.getOrderLineItems()
                .stream()
                .map(orderLineItemRequest -> {
                    final Menu findMenu = menuRepository.findMenuById(orderLineItemRequest.getMenuId());
                    return OrderLineItem.ofWithoutOrder(findMenu, new Quantity(orderLineItemRequest.getQuantity()));
                }).collect(Collectors.toList());
    }

    public List<Order> list() {
        final List<Order> orders = orderRepository.findAll();

        for (final Order order : orders) {
            order.addOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final String orderStatus) {
        final Order savedOrder = orderRepository.findOrderById(orderId);
        final OrderStatus findOrderStatus = OrderStatus.valueOf(orderStatus);
        savedOrder.changeOrderStatus(findOrderStatus);

        orderRepository.save(savedOrder);

        savedOrder.addOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));
        return savedOrder;
    }
}
