package kitchenpos.order.application;

import java.util.List;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.Menus;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderMenuDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderMenus;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final OrderMenuDao orderMenuDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderTableDao orderTableDao, final OrderMenuDao orderMenuDao) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.orderMenuDao = orderMenuDao;
    }

    @Transactional
    public Order create(final Order request) {
        final OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        final Menus menus = menuDao.findAllByIdIn(request.getMenuIds());
        final OrderMenus orderMenus = orderMenuDao.save(OrderMenus.createByMenus(menus));
        return orderDao.save(request.init(orderTable, orderMenus));
    }

    public List<Order> list() {
        return orderDao.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        final Order orderWithNewOrderStatus = savedOrder.updateOrderStatus(order.getOrderStatus());
        return orderDao.update(orderWithNewOrderStatus);
    }
}
