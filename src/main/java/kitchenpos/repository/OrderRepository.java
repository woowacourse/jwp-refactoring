package kitchenpos.repository;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderRepository {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final OrderLineItemDao orderLineItemDao;

    public OrderRepository(final MenuDao menuDao, final OrderDao orderDao, final OrderTableDao orderTableDao,
                           final OrderLineItemDao orderLineItemDao) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.orderLineItemDao = orderLineItemDao;
    }

    @Transactional
    public Order save(final Order entity) {
        validateOrderLineItems(entity);

        OrderTable orderTable = orderTableDao.getById(entity.getOrderTableId());
        validateEmptyOrderTable(orderTable);

        Order savedOrder = orderDao.save(new Order(orderTable.getId(), entity.getOrderLineItems()));
        List<OrderLineItem> savedOrderLineItems = saveOrderLineItems(savedOrder);

        savedOrder.addOrderLineItems(savedOrderLineItems);

        return savedOrder;
    }

    private void validateEmptyOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderLineItems(final Order entity) {
        final List<OrderLineItem> orderLineItems = entity.getOrderLineItems();

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(toList());

        if (orderLineItems.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderLineItem> saveOrderLineItems(final Order order) {
        return order.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItem(order.getId(), it.getMenuId(), it.getQuantity()))
                .map(orderLineItemDao::save)
                .collect(toList());
    }
}
