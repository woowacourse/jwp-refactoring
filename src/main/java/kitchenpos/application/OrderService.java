package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.exception.MenuNotFoundException;
import kitchenpos.application.exception.OrderNotFoundException;
import kitchenpos.application.exception.OrderTableNotFoundException;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.ordertable.repository.OrderTableRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.ui.dto.request.CreateOrderLineItemRequest;
import kitchenpos.ui.dto.request.CreateOrderRequest;
import kitchenpos.ui.dto.request.UpdateOrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                                                          .orElseThrow(OrderTableNotFoundException::new);
        final List<OrderLineItem> orderLineItems = findOrderLineItems(request);
        final Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), orderLineItems);

        return orderRepository.save(order);
    }

    private List<OrderLineItem> findOrderLineItems(final CreateOrderRequest request) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();

        for (final CreateOrderLineItemRequest orderLineItemRequest : request.getOrderLineItems()) {
            final Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                                            .orElseThrow(MenuNotFoundException::new);

            orderLineItems.add(new OrderLineItem(menu, orderLineItemRequest.getQuantity()));
        }

        return orderLineItems;
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final UpdateOrderStatusRequest request) {
        final Order persistOrder = orderRepository.findById(orderId)
                                                  .orElseThrow(OrderNotFoundException::new);
        persistOrder.updateOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));

        return persistOrder;
    }
}
