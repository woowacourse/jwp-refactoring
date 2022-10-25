package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
        MenuDao menuDao,
        OrderDao orderDao,
        OrderLineItemDao orderLineItemDao,
        OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(OrderRequest orderRequest) {
        List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException("메뉴 상품이 1개 이상 있어야 합니다.");
        }

        final List<Long> menuIds = orderLineItemRequests.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());

        if (orderLineItemRequests.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 포함되어 있습니다.");
        }

        OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에선 주문을 생성할 수 없습니다.");
        }

        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now());

        Order savedOrder = orderDao.save(order);

        Long orderId = savedOrder.getId();
        List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItemRequest.toEntity(orderId)));
        }
        savedOrder.addOrderLineItems(savedOrderLineItems);

        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> list() {
        List<Order> orders = orderDao.findAll();

        for (Order order : orders) {
            order.addOrderLineItems(orderLineItemDao.findAllByOrderId(order.getId()));
        }

        return orders.stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order savedOrder = orderDao.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException("완료 주문은 상태를 변경할 수 없습니다.");
        }

        savedOrder.updateStatus(orderStatus.name());

        orderDao.save(savedOrder);

        savedOrder.addOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return OrderResponse.from(savedOrder);
    }
}
