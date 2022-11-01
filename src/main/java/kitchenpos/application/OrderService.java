package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.application.dto.request.OrderRequest;
import kitchenpos.application.dto.request.OrderStatusUpdateRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;

@Service
@Transactional
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderRepository orderRepository;
    private final OrderTableDao orderTableDao;

    public OrderService(MenuRepository menuRepository, OrderDao orderDao, OrderLineItemDao orderLineItemDao,
                        OrderRepository orderRepository, OrderTableDao orderTableDao) {
        this.menuRepository = menuRepository;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderRepository = orderRepository;
        this.orderTableDao = orderTableDao;
    }

    public OrderResponse create(OrderRequest request) {
        Order order = request.toOrder();
        validateOrderItemSize(order.getOrderLineItems());
        validateOrderTable(order);

        return new OrderResponse(orderRepository.save(order));
    }

    private void validateOrderTable(Order order) {
        OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있습니다.");
        }
    }

    private void validateOrderItemSize(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }

        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 있습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(Long orderId, OrderStatusUpdateRequest request) {
        Order savedOrder = validateExistOrder(orderId);

        OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        savedOrder.changeStatus(orderStatus);

        orderRepository.update(savedOrder);
        savedOrder.addOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));

        return new OrderResponse(savedOrder);
    }

    private Order validateExistOrder(Long orderId) {
        return orderDao.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
    }
}
