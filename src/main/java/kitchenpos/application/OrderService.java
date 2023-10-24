package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.ui.dto.ChangeOrderStatusRequest;
import kitchenpos.ui.dto.CreateOrderLineItemRequest;
import kitchenpos.ui.dto.CreateOrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final CreateOrderRequest request) {
        final OrderTable orderTable = validateRequestAndProcessOrderTable(request);
        final List<OrderLineItem> orderLineItems = processOrderLineItems(request);

        final Order order = Order.createNewOrder(orderTable);
        final Order savedOrder = orderRepository.save(order);
        savedOrder.addOrderLineItems(new OrderLineItems(orderLineItems));

        return savedOrder;
    }

    private OrderTable validateRequestAndProcessOrderTable(final CreateOrderRequest request) {
        final List<CreateOrderLineItemRequest> createOrderLineItemRequests = request.getOrderLineItems();
        if (createOrderLineItemRequests.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }

        return orderTableRepository.findById(request.getOrderTableId())
                                   .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
    }

    private List<OrderLineItem> processOrderLineItems(final CreateOrderRequest request) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final CreateOrderLineItemRequest createOrderLineItemRequest : request.getOrderLineItems()) {
            final Menu findMenu = menuRepository.findById(createOrderLineItemRequest.getMenuId())
                                                .orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다."));

            final OrderLineItem orderLineItem = new OrderLineItem(findMenu, createOrderLineItemRequest.getQuantity());
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
