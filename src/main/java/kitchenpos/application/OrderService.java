package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menus;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderChangeRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemResponse;
import kitchenpos.dto.OrderResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        final MenuRepository menuRepository,
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
    public OrderResponse create(final OrderCreateRequest orderCreateRequest) {
        OrderLineItems orderLineItems
            = new OrderLineItems(orderCreateRequest.getOrderLineItems());
        Menus menus = new Menus(orderLineItems.toMenus());
        validateMenuIds(menus);

        final OrderTable orderTable = orderTableRepository
            .findById(orderCreateRequest.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        final Order savedOrder = saveOrder(orderTable);
        List<OrderLineItemResponse> orderLineItemResponses
            = createOrderLineItemResponses(orderLineItems, savedOrder);

        return OrderResponse.of(savedOrder, orderLineItemResponses);
    }

    private void validateMenuIds(Menus menus) {
        if (!menus.isSameSize(menuRepository.countByIdIn(menus.extractIds()))) {
            throw new IllegalArgumentException();
        }
    }

    private Order saveOrder(OrderTable orderTable) {
        Order order = Order.of(orderTable, OrderStatus.COOKING, LocalDateTime.now());

        return orderRepository.save(order);
    }

    private List<OrderLineItemResponse> createOrderLineItemResponses(
        OrderLineItems orderLineItems, Order savedOrder) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItem orderLineItem : orderLineItems.getOrderLineItems()) {
            orderLineItem.updateOrderLineItem(savedOrder);
            OrderLineItem savedOrderLineItem = orderLineItemRepository.save(orderLineItem);

            savedOrderLineItems.add(savedOrderLineItem);
        }

        return OrderLineItemResponse.toResponseList(savedOrderLineItems);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        final List<OrderResponse> orderResponses = new ArrayList<>();

        for (final Order order : orders) {
            List<OrderLineItem> orderLineItems
                = orderLineItemRepository.findAllByOrderId(order.getId());
            OrderResponse orderResponse
                = OrderResponse.of(order, OrderLineItemResponse.toResponseList(orderLineItems));

            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
        final OrderChangeRequest orderChangeRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        final OrderStatus orderStatus = OrderStatus.valueOf(orderChangeRequest.getOrderStatus());
        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrderId(orderId);

        savedOrder.updateOrder(savedOrder.getId(), savedOrder.getOrderTable(), orderStatus,
            savedOrder.getOrderedTime());

        return OrderResponse.of(savedOrder, OrderLineItemResponse.toResponseList(orderLineItems));
    }
}
