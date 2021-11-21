package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderStatusRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.exception.NotFoundMenuException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.OrderTableEmptyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderLineItemRepository orderLineItemRepository,
            OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {

        Order order = getOrder(orderCreateRequest);

        return OrderResponse.create(orderRepository.save(order));
    }

    private Order getOrder(OrderCreateRequest orderCreateRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(NotFoundOrderTableException::new);

        checkOrderTableEmpty(orderTable);

        List<OrderLineItem> orderLineItems = getOrderLineItems(orderCreateRequest);

        return new Order.OrderBuilder()
                .setOrderTable(orderTable)
                .setOrderLineItems(orderLineItems)
                .setOrderStatus(OrderStatus.COOKING.name())
                .setOrderedTime(LocalDateTime.now())
                .build();
    }

    private void checkOrderTableEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new OrderTableEmptyException();
        }
    }

    private List<OrderLineItem> getOrderLineItems(OrderCreateRequest orderCreateRequest) {

        List<Menu> menus = getMenus(orderCreateRequest);
        List<OrderLineItemRequest> orderLineItemRequests = orderCreateRequest.getOrderLineItemRequests();

        List<OrderLineItem> orderLineItems = new ArrayList<>();

        for (int i = 0; i < orderLineItemRequests.size(); i++) {
            orderLineItems.add(new OrderLineItem.OrderLineItemBuilder()
                    .setMenu(menus.get(i))
                    .setQuantity(orderLineItemRequests.get(i).getQuantity())
                    .build()
            );
        }

        return orderLineItems;
    }

    private List<Menu> getMenus(OrderCreateRequest orderCreateRequest) {
        List<Menu> menus = menuRepository.findAllById(orderCreateRequest.getMenuIds());

        checkMenuSize(orderCreateRequest.getOrderLineItemRequests().size(), menus.size());

        return menus;
    }

    private void checkMenuSize(int orderLineItemSize, int menuSize) {
        if (orderLineItemSize != menuSize) {
            throw new NotFoundMenuException();
        }
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        for (final Order order : orders) {
            order.changeOrderLineItems(orderLineItemRepository.findAllByOrderId(order.getId()));
        }

        return orders.stream()
                .map(OrderResponse::create)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest orderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(OrderStatus.findByString(orderStatusRequest.getOrderStatus()));

        orderRepository.save(savedOrder);

        savedOrder.changeOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));

        return OrderResponse.create(savedOrder);
    }
}
