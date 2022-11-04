package kitchenpos.order.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.jdbc.JdbcTemplateOrderDao;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplateOrderDao orderDao;
    private final OrderLineItemRepository itemRepository;
    private final MenuRepository menuRepository;

    public OrderRepositoryImpl(JdbcTemplateOrderDao orderDao, OrderLineItemRepository itemRepository,
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
            saveItem(items, item.placeOrderId(save.getId()));
        }
        save.placeOrderLineItems(new OrderLineItems(items));
    }

    private void saveItem(List<OrderLineItem> items, OrderLineItem item) {
        if (item.getSeq() == null) {
            menuRepository.findById(item.getMenuId());
            items.add(itemRepository.save(item));
            return;
        }
        items.add(itemRepository.findById(item.getSeq()));
    }

    @Override
    public Order findById(Long id) {
        return orderDao.findById(id)
                .orElseThrow(() -> new InvalidDataAccessApiUsageException("해당 아이디의 주문은 존재하지 않는다."))
                .placeOrderLineItems(new OrderLineItems(itemRepository.findAllByOrderId(id)));
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = orderDao.findAll();
        for (Order order : orders) {
            order.placeOrderLineItems(new OrderLineItems(itemRepository.findAllByOrderId(order.getId())));
        }
        return orders;
    }

    @Override
    public void validateComplete(List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("단체 지정 속 모든 테이블들의 주문이 있다면 COMPLETION 상태여야 한다.");
        }
    }

    @Override
    public void validateOrdersCompleted(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("테이블의 주문이 있다면 COMPLETION 상태여야 한다.");
        }
    }
}
