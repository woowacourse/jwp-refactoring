package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.ChangeOrderStatusRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public OrderService(final OrderDao orderDao,
                        final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable table = orderTableDao.findById(orderRequest.getOrderTableId())
                .validateTableIsFull();

        final Order order = new Order(table.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                orderRequest.toOrderLineItems());

        return OrderResponse.from(orderDao.save(order));
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderDao.findAll();
        return orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest statusRequest) {
        final Order order = orderDao.findById(orderId)
                .placeOrderStatus(statusRequest.getOrderStatus().name());
        Order save = orderDao.save(order);
        return OrderResponse.from(save);
    }
}
