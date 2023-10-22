package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.vo.order.ChangeOrderStatusRequest;
import kitchenpos.vo.order.OrderLineItemRequest;
import kitchenpos.vo.order.OrderRequest;
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
    public Order create(final OrderRequest orderRequest) {
        OrderTable orderTable = getOrderTable(orderRequest);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈테이블은 주문을 등록할 수 없습니다.");
        }

        Order order = new Order(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        setOrderLineItems(orderRequest.getOrderLineItems(), savedOrder);

        return savedOrder;
    }

    private OrderTable getOrderTable(OrderRequest orderRequest) {
        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItems())) {
            throw new IllegalArgumentException("빈 주문 항목으로는 주문을 등록할 수 없습니다.");
        }
        if (orderRequest.getOrderLineItems().stream()
                .anyMatch(orderLineItem -> !menuRepository.existsById(orderLineItem.getMenuId()))) {
            throw new IllegalArgumentException("등록되지 않은 메뉴에는 주문을 등록할 수 없습니다.");
        }
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 테이블에는 주문을 등록할 수 없습니다."));
        return orderTable;
    }

    private void setOrderLineItems(List<OrderLineItemRequest> orderLineItemsRequest, Order savedOrder) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemsRequest) {
            Menu savedMenu = menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(IllegalArgumentException::new);
            OrderLineItem orderLineItem = new OrderLineItem(savedOrder, savedMenu, orderLineItemRequest.getQuantity());
            savedOrderLineItems.add(orderLineItemRepository.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest changeOrderStatusRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 주문은 주문 상태를 바꿀 수 없습니다."));
        if (savedOrder.isCompletionStatus()) {
            throw new IllegalArgumentException("완료 상태의 주문은 상태 변경이 불가능합니다.");
        }
        OrderStatus orderStatus = OrderStatus.valueOf(changeOrderStatusRequest.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        orderRepository.save(savedOrder);

        return savedOrder;
    }
}
