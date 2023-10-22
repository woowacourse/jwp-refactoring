package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.service.OrderCreateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderCreateService orderCreateService;

    public OrderService(
            final MenuDao menuDao,
            final OrderDao orderDao,
            OrderCreateService orderCreateService) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderCreateService = orderCreateService;
    }

    @Transactional
    public Order create(final Long orderTableId, final Order order) {
        final List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("한번의 주문에서 중복 메뉴를 주문할 수 없습니다.");
        }

        return orderCreateService.create(orderTableId, order);
    }

    public List<Order> list() {
        return orderDao.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderDao.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        savedOrder.changeOrderStatus(orderStatus);

        return savedOrder;
    }
}
