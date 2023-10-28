package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.dto.ChangeOrderStatusRequest;
import kitchenpos.order.application.dto.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            MenuRepository menuRepository,
            OrderRepository orderRepository,
            OrderLineItemRepository orderLineItemRepository,
            OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(OrderRequest orderRequest) {
        Order order = new Order(getOrderTable(orderRequest), OrderStatus.COOKING.name(), LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        setOrderLineItems(orderRequest, savedOrder);

        return savedOrder;
    }

    private OrderTable getOrderTable(OrderRequest orderRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 테이블에는 주문을 등록할 수 없습니다."));

        return orderTable;
    }

    private void setOrderLineItems(OrderRequest orderRequest, Order savedOrder) {
        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItems())) {
            throw new IllegalArgumentException("빈 주문 항목으로는 주문을 등록할 수 없습니다.");
        }
        if (orderRequest.getOrderLineItems().stream()
                .anyMatch(orderLineItem -> !menuRepository.existsById(orderLineItem.getMenuId()))) {
            throw new IllegalArgumentException("등록되지 않은 메뉴에는 주문을 등록할 수 없습니다.");
        }

        List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItems()) {
            Menu savedMenu = menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(IllegalArgumentException::new);
            OrderLineItem orderLineItem = new OrderLineItem(savedMenu.getId(), orderLineItemRequest.getQuantity());
            savedOrderLineItems.add(orderLineItem);
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, ChangeOrderStatusRequest changeOrderStatusRequest) {
        Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 주문은 주문 상태를 바꿀 수 없습니다."));

        OrderStatus orderStatus = OrderStatus.valueOf(changeOrderStatusRequest.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus.name());

        orderRepository.save(savedOrder);

        return savedOrder;
    }
}
