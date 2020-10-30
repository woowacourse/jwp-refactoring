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
import kitchenpos.dao.OrderMenuDao;
import kitchenpos.dao.TableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderMenu;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Table;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderMenuDao orderMenuDao;
    private final TableDao tableDao;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            final OrderMenuDao orderMenuDao,
            final TableDao tableDao
    ) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderMenuDao = orderMenuDao;
        this.tableDao = tableDao;
    }

    @Transactional
    public Order create(final Order order) {
        final List<OrderMenu> orderMenus = order.getOrderMenus();

        if (CollectionUtils.isEmpty(orderMenus)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderMenus.stream()
                .map(OrderMenu::getMenuId)
                .collect(Collectors.toList());

        if (orderMenus.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        order.setId(null);

        final Table table = tableDao.findById(order.getTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (table.isEmpty()) {
            throw new IllegalArgumentException();
        }

        order.setTableId(table.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        final Order savedOrder = orderDao.save(order);

        final Long orderId = savedOrder.getId();
        final List<OrderMenu> savedOrderMenus = new ArrayList<>();
        for (final OrderMenu orderMenu : orderMenus) {
            orderMenu.setOrderId(orderId);
            savedOrderMenus.add(orderMenuDao.save(orderMenu));
        }
        savedOrder.setOrderMenus(savedOrderMenus);

        return savedOrder;
    }

    public List<Order> list() {
        final List<Order> orders = orderDao.findAll();

        for (final Order order : orders) {
            order.setOrderMenus(orderMenuDao.findAllByOrderId(order.getId()));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final Order order) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.setOrderStatus(orderStatus.name());

        orderDao.save(savedOrder);

        savedOrder.setOrderMenus(orderMenuDao.findAllByOrderId(orderId));

        return savedOrder;
    }
}
