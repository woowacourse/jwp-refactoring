package kitchenpos.application;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderValidator;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.dto.ChangeOrderStatusRequest;
import kitchenpos.dto.CreateOrderLineItemRequest;
import kitchenpos.dto.CreateOrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderValidator orderValidator
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public Order create(final CreateOrderRequest request) {
        validateOrderLineItems(request);

        final Order order = Order.createNewOrder(request.getOrderTableId(), orderValidator);
        final List<OrderLineItem> orderLineItems = getOrderLineItems(request);

        final Order savedOrder = orderRepository.save(order);
        savedOrder.addOrderLineItems(new OrderLineItems(orderLineItems));

        return savedOrder;
    }

    private void validateOrderLineItems(final CreateOrderRequest request) {
        final List<CreateOrderLineItemRequest> createOrderLineItemRequests = request.getOrderLineItems();
        if (createOrderLineItemRequests.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }
    }

    private List<OrderLineItem> getOrderLineItems(final CreateOrderRequest request) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final CreateOrderLineItemRequest createOrderLineItemRequest : request.getOrderLineItems()) {
            final Long menuId = createOrderLineItemRequest.getMenuId();
            if (!menuRepository.existsById(menuId)) {
                throw new IllegalArgumentException("메뉴가 존재하지 않습니다.");
            }

            final OrderLineItem orderLineItem = new OrderLineItem(menuId, createOrderLineItemRequest.getQuantity());
            orderLineItems.add(orderLineItem);
        }

        return orderLineItems;
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                                                .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());

        savedOrder.changeOrderStatus(orderStatus);

        return savedOrder;
    }
}
