package kitchenpos.dao.repository;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.jdbctemplate.JdbcTemplateOrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository implements OrderDao {

    private final JdbcTemplateOrderDao orderDao;
    private final OrderLineItemRepository itemRepository;
    private final MenuRepository menuRepository;

    public OrderRepository(JdbcTemplateOrderDao orderDao, OrderLineItemRepository itemRepository,
                           MenuRepository menuRepository) {
        this.orderDao = orderDao;
        this.itemRepository = itemRepository;
        this.menuRepository = menuRepository;
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
            item.placeOrderId(save.getId());
            saveItem(items, item);
        }
        save.placeOrderLineItems(items);
    }

    private void saveItem(List<OrderLineItem> items, OrderLineItem item) {
        menuRepository.findById(item.getMenuId());
        if (item.getSeq() == null) {
            items.add(itemRepository.save(item));
            return;
        }
        items.add(itemRepository.findById(item.getSeq()));
    }

    @Override
    public Order findById(Long id) {
        Order order = orderDao.findById(id)
                .orElseThrow(() -> new InvalidDataAccessApiUsageException("해당 아이디의 주문은 존재하지 않는다."));
        order.placeOrderLineItems(itemRepository.findAllByOrderId(order.getId()));
        return order;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = orderDao.findAll();
        for (Order order : orders) {
            order.placeOrderLineItems(itemRepository.findAllByOrderId(order.getId()));
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
