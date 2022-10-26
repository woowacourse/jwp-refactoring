package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableRepository;
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
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        MenuDao menuDao,
        OrderDao orderDao,
        OrderLineItemDao orderLineItemDao,
        OrderTableRepository orderTableRepository
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(OrderRequest orderRequest) {
        List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItems();

        List<Long> menuIds = extractMenuIds(orderLineItemRequests);
        validateMenuExist(orderLineItemRequests, menuIds);

        OrderTable orderTable = getOrderTableById(orderRequest.getOrderTableId());
        validateTableEmpty(orderTable);

        Order savedOrder = createOrder(orderTable);
        addItems(orderLineItemRequests, savedOrder, savedOrder.getId());
        return OrderResponse.from(savedOrder);
    }

    private OrderTable getOrderTableById(Long tableId) {
        return orderTableRepository.findById(tableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));
    }

    private void validateTableEmpty(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에선 주문을 생성할 수 없습니다.");
        }
    }

    private Order createOrder(OrderTable orderTable) {
        return orderDao.save(new Order(
            orderTable.getId(),
            OrderStatus.COOKING.name(),
            LocalDateTime.now())
        );
    }

    private List<Long> extractMenuIds(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());
    }

    private void addItems(List<OrderLineItemRequest> orderLineItemRequests, Order savedOrder, Long orderId) {
        List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItemRequest.toEntity(orderId)));
        }
        savedOrder.addOrderLineItems(savedOrderLineItems);
    }

    private void validateMenuExist(List<OrderLineItemRequest> orderLineItemRequests, List<Long> menuIds) {
        if (orderLineItemRequests.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 포함되어 있습니다.");
        }
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
        Order savedOrder = getOrderById(orderId);

        savedOrder.updateStatus(orderStatus.name());
        orderDao.save(savedOrder);
        savedOrder.addOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));
        return OrderResponse.from(savedOrder);
    }

    private Order getOrderById(Long orderId) {
        return orderDao.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
    }
}
