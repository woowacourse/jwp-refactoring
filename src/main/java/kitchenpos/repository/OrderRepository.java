package kitchenpos.repository;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class OrderRepository {

    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public OrderRepository(final MenuDao menuDao,
                           final OrderDao orderDao,
                           final OrderTableDao orderTableDao) {
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public Order save(final Order entity) {
        validateOrderLineItems(entity);

        OrderTable orderTable = orderTableDao.getById(entity.getOrderTableId());
        validateEmptyOrderTable(orderTable);

        return orderDao.save(new Order(orderTable.getId(), entity.getOrderLineItems()));
    }

    private void validateOrderLineItems(final Order entity) {
        final List<Long> menuIds = entity.getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(toList());

        if (menuIds.size() != menuDao.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateEmptyOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public Order getById(final Long id) {
        return orderDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Order> findAll() {
        return orderDao.findAll();
    }
}
