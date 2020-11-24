package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.TableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemResponse;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusChangeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final TableDao tableDao;

    public OrderService(
        final MenuDao menuDao,
        final OrderDao orderDao,
        final OrderLineItemDao orderLineItemDao,
        final TableDao tableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.tableDao = tableDao;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {
        final Order savedOrder = saveOrder(
            new Order(request.getOrderTableId(), OrderStatus.COOKING.name(), LocalDateTime.now()));

        OrderLineItems orderLineItems = OrderLineItems.from(request.getOrderLineItems(), savedOrder);

        validateMenusAreExist(orderLineItems.getMenuIds());

        final List<OrderLineItem> savedOrderLineItems = saveOrderLineItems(orderLineItems);

        return OrderResponse.of(savedOrder, OrderLineItemResponse.of(savedOrderLineItems));
    }

    private Order saveOrder(Order order) {
        validateTableIdIsExist(order.getOrderTableId());

        return orderDao.save(order);
    }

    private void validateTableIdIsExist(Long tableId) {
        tableDao.findById(tableId)
            .filter(table -> !table.isEmpty())
            .orElseThrow(() -> new IllegalArgumentException("ID가 " + tableId + "인 테이블이 존재하지 않습니다."));
    }

    private void validateMenusAreExist(List<Long> menuIds) {
        if (menuIds.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴를 주문할 수 없습니다.");
        }
    }

    private List<OrderLineItem> saveOrderLineItems(OrderLineItems orderLineItems) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();

        for (final OrderLineItem orderLineItem : orderLineItems.getOrderLineItems()) {
            OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);
            savedOrderLineItems.add(savedOrderLineItem);
        }
        return Collections.unmodifiableList(savedOrderLineItems);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = orderDao.findById(orderId)
            .filter(Order::isNotCompleted)
            .orElseThrow(() -> new IllegalArgumentException(
                "식사가 완료된 테이블의 주문 상태를 변경할 수 없습니다."));

        final OrderStatus orderStatus = OrderStatus.of(request.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

        orderDao.save(savedOrder);
        return OrderResponse.of(savedOrder, OrderLineItemResponse.of(
            orderLineItemDao.findAllByOrderId(orderId)));
    }

    public List<OrderResponse> list() {
        return orderDao.findAll().stream()
            .map(order -> OrderResponse.of(order, OrderLineItemResponse.of(
                orderLineItemDao.findAllByOrderId(order.getId()))))
            .collect(Collectors.toList());
    }
}
