package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.application.request.OrderRequest;
import kitchenpos.order.application.response.OrderResponse;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.application.validator.OrderValidator;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderDao orderDao;
    private final OrderValidator orderValidator;

    public OrderService(final OrderDao orderDao, final OrderValidator orderValidator) {
        this.orderDao = orderDao;
        this.orderValidator = orderValidator;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        Order order = new Order(request.getOrderTableId(), request.toOrderLineItems());
        orderValidator.validate(order);

        return OrderResponse.from(orderDao.save(order));
    }

    public List<OrderResponse> list() {
        return OrderResponse.fromAll(orderDao.findAll());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
        final Order savedOrder = orderDao.getById(orderId);
        savedOrder.changeStatus(request.getOrderStatus());

        return OrderResponse.from(savedOrder);
    }
}
