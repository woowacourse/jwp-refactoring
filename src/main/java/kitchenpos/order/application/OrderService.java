package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.response.OrderResponse;
import kitchenpos.order.ui.request.OrderCreateRequest;
import kitchenpos.order.ui.request.OrderStatusChangeRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.dao.OrderTableDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableDao orderTableDao;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository,
                        final OrderTableDao orderTableDao) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final Order request) {
        final Order order = Order.of(getOrderTableId(request), request.getOrderLineItems());
        order.checkActualOrderLineItems(menuRepository.countByIdIn(order.getMenuIds()));
        return OrderResponse.from(orderRepository.save(order));
    }

    private Long getOrderTableId(final Order request) {
        final Long orderTableId = request.getOrderTableId();
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        validateOrderTableEmpty(orderTable);
        return orderTable.getId();
    }

    private static void validateOrderTableEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어 있을 수 없습니다.");
        }
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrder.changeOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));
        return OrderResponse.from(orderRepository.save(savedOrder));
    }
}
