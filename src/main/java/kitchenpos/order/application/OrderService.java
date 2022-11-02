package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.presentation.dto.request.OrderLineItemRequest;
import kitchenpos.order.presentation.dto.request.OrderRequest;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.TableRepository;
import kitchenpos.order.specification.OrderSpecification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderSpecification orderSpecification;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final TableRepository tableRepository;

    public OrderService(
            OrderSpecification orderSpecification,
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderLineItemRepository orderLineItemRepository,
            TableRepository tableRepository) {
        this.orderSpecification = orderSpecification;
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public Order create(OrderRequest request) {

        orderSpecification.validateCreate(request);

        OrderTable orderTable = tableRepository.findById(request.getOrderTableId()).get();
        Order order = new Order(orderTable);
        order.validate();

        List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();

        List<OrderLineItem> orderLineItems = convertOrderLineItems(order, orderLineItemRequests);
        order.mapOrderLineItems(orderLineItems);

        return orderRepository.save(order);
    }

    public List<Order> list() {

        final List<Order> orders = orderRepository.findAll();

        for (final Order order : orders) {
            order.mapOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, OrderRequest request) {

        Order savedOrder = orderRepository.findWithOrderItemsById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        orderSpecification.validateChangeOrderStatus(savedOrder);

        OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.mapOrderStatus(orderStatus);

        orderRepository.save(savedOrder);

        return savedOrder;
    }

    private List<OrderLineItem> convertOrderLineItems(Order order, List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(it -> new OrderLineItem(order, it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
    }
}
