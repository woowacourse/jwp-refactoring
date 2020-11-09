package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderMenuDao;
import kitchenpos.dao.TableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderMenu;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Table;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderMenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public Order create(final OrderCreateRequest orderCreateRequest) {

        List<OrderMenuRequest> orderMenuRequests = orderCreateRequest.getOrderMenuRequests();

        if (orderMenuRequests.isEmpty()){
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderMenuRequests.stream()
                .map(OrderMenuRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderMenuRequests.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final Table table = tableDao.findById(orderCreateRequest.getTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (table.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final Order savedOrder = orderDao.save(new Order(table.getId()));

        final Long orderId = savedOrder.getId();

        List<OrderMenu> orderMenus = orderMenuRequests.stream()
            .map(request -> new OrderMenu(orderId, request.getMenuId(), request.getQuantity()))
            .collect(Collectors.toList());

        for (final OrderMenu orderMenu : orderMenus) {
            orderMenuDao.save(orderMenu);
        }
        return savedOrder;
    }

    public List<Order> list() {
        return orderDao.findAll();
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

        return savedOrder;
    }
}
