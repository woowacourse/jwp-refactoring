package kitchenpos.dao.repository;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.jdbctemplate.JdbcTemplateOrderDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository implements OrderDao {

    private final JdbcTemplateOrderDao orderDao;
    private final OrderLineItemRepository itemRepository;

    public OrderRepository(JdbcTemplateOrderDao orderDao, OrderLineItemRepository itemRepository) {
        this.orderDao = orderDao;
        this.itemRepository = itemRepository;
    }

    @Override
    public Order save(Order entity) {
        Order save = orderDao.save(entity);

        if (entity.getId() == null) {
            saveItems(entity, save);
        }
        return save;
    }

    private void saveItems(Order entity, Order save) {
        List<OrderLineItem> items = new ArrayList<>();
        for (OrderLineItem item : entity.getOrderLineItems()) {
            item.updateOrderId(save.getId());
            items.add(itemRepository.save(item));
        }
        save.updateOrderLineItems(items);
    }

    @Override
    public Order findById(Long id) {
        Order order = orderDao.findById(id)
                .orElseThrow(() -> new InvalidDataAccessApiUsageException("해당 아이디의 주문은 존재하지 않는다."));
        order.updateOrderLineItems(itemRepository.findAllByOrderId(order.getId()));
        return order;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = orderDao.findAll();
        for (Order order : orders) {
            order.updateOrderLineItems(itemRepository.findAllByOrderId(order.getId()));
        }
        return orders;
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<String> orderStatuses) {
        return orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<String> orderStatuses) {
        return orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, orderStatuses);
    }
}
