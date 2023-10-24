package kitchenpos.domain.order.service;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.table.OrderTable;
import org.springframework.stereotype.Component;

@Component
public class OrderCreateService {

    private final OrderTableDao orderTableDao;
    private final OrderDao orderDao;

    public OrderCreateService(OrderTableDao orderTableDao, OrderDao orderDao) {
        this.orderTableDao = orderTableDao;
        this.orderDao = orderDao;
    }

    public Order create(Long tableId, Order order) {

        OrderTable orderTable = orderTableDao.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        orderTable.createdOrder(order);

        return orderDao.save(order);
    }
}
