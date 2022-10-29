package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderLineItemDao orderLineItemDao,
            final OrderTableDao orderTableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    private static List<Long> getMenuIds(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        validateOrderLineItems(request);

        OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문을 생성하기 위한 테이블이 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비었습니다");
        }

        Order savedOrder = orderDao.save(
                new Order(orderTable.getId(), OrderStatus.COOKING, request.getOrderLineItems())
        );
        return new OrderResponse(savedOrder);
    }

    private void validateOrderLineItems(OrderRequest request) {
        List<OrderLineItem> orderLineItems = request.getOrderLineItems();

        if (orderLineItems.size() != menuDao.countByIdIn(getMenuIds(orderLineItems))) {
            throw new IllegalArgumentException("주문정보가 존재하지 않습니다.");
        }
    }

    public List<OrderResponse> list() {
        return orderDao.findAll().stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("상태를 변화시키기 위한 주문이 없습니다."));

        if (savedOrder.equalStatus(OrderStatus.COMPLETION)) {
            throw new IllegalArgumentException("주문이 완료된 상태이므로 상태를 변화시킬 수 없습니다.");
        }

        savedOrder.changeOrderStatus(orderStatus);

        savedOrder.setOrderLineItems(orderLineItemDao.findAllByOrderId(orderId));
        return new OrderResponse(orderDao.save(savedOrder));
    }
}
