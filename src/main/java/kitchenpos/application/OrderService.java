package kitchenpos.application;

import static java.time.LocalDateTime.now;
import static kitchenpos.domain.order.OrderStatus.COOKING;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.repository.order.OrderRepository;
import kitchenpos.repository.order.OrderLineItemRepository;
import kitchenpos.repository.order.TableRepository;
import kitchenpos.repository.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.specification.OrderSpecification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
            TableRepository tableRepository
    ) {
        this.orderSpecification = orderSpecification;
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public Order create(OrderRequest request) {

        orderSpecification.validateCreate(request);

        OrderTable orderTable = tableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("존재하는 주문 테이블에 대해서만 주문이 가능합니다."));

        Order order = new Order(orderTable);

        List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();

        List<OrderLineItem> orderLineItems = convertOrderLineItems(order, orderLineItemRequests);
        order.mapOrderLineItems(orderLineItems);

        return orderRepository.save(order);
    }

    public List<Order> list() {

        final List<Order> orders = orderRepository.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, OrderRequest request) {

        Order savedOrder = orderRepository.findWithOrderItemsById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        orderSpecification.validateChangeOrderStatus(orderId, savedOrder);

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus);

        orderRepository.save(savedOrder);

        return savedOrder;
    }

    private List<OrderLineItem> convertOrderLineItems(Order order, List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(it -> new OrderLineItem(order, it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
    }
}
