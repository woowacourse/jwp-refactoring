package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
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
        validateOrderLineItems(request);
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                                   .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        return saveOrder(request, orderTable);
    }

    private void validateOrderLineItems(final CreateOrderRequest request) {
        final List<CreateOrderLineItemRequest> createOrderLineItemRequests = request.getOrderLineItems();
        if (createOrderLineItemRequests.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }
    }

    private Order saveOrder(final CreateOrderRequest request, final OrderTable orderTable) {
        final Order order = Order.createNewOrder(orderTable);
        final List<OrderLineItem> orderLineItems = getOrderLineItems(request);

        final Order savedOrder = orderRepository.save(order);
        savedOrder.addOrderLineItems(new OrderLineItems(orderLineItems));

        return savedOrder;
    }

    private List<OrderLineItem> getOrderLineItems(final CreateOrderRequest request) {
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
