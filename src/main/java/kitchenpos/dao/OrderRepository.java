package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class OrderRepository implements OrderDao {

    private final JdbcTemplateOrderDao orderDao;
    private final MenuDao menuDao;
    private final OrderTableDao orderTableDao;
    private final OrderLineItemDao orderLineItemDao;

    public OrderRepository(JdbcTemplateOrderDao orderDao, MenuDao menuDao, OrderTableDao orderTableDao,
                           OrderLineItemDao orderLineItemDao) {
        this.orderDao = orderDao;
        this.menuDao = menuDao;
        this.orderTableDao = orderTableDao;
        this.orderLineItemDao = orderLineItemDao;
    }

    @Override
    public Order save(Order entity) {
        orderTableDao.findById(entity.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        List<OrderLineItem> orderLineItems = entity.getOrderLineItems();
        if (orderLineItems.size() != getMenuCount(entity)) {
            throw new IllegalArgumentException();
        }

        Order order = orderDao.save(entity);
        order.setOrderLineItems(saveOrderLineItems(orderLineItems, order));
        return order;
    }

    private long getMenuCount(Order entity) {
        return menuDao.countByIdIn(entity.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList()));
    }

    private ArrayList<OrderLineItem> saveOrderLineItems(List<OrderLineItem> orderLineItems, Order order) {
        ArrayList<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(order.getId());
            savedOrderLineItems.add(orderLineItemDao.save(orderLineItem));
        }
        return savedOrderLineItems;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses) {
        return false;
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses) {
        return false;
    }
}
