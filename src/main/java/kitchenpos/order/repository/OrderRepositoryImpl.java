package kitchenpos.order.repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderLineItemsMapper;
import kitchenpos.order.domain.OrderMapper;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.jdbc.JdbcTemplateOrderDao;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplateOrderDao orderDao;
    private final OrderLineItemRepository itemRepository;
    private final MenuRepository menuRepository;
    private final OrderMapper orderMapper;
    private final OrderLineItemsMapper orderLineItemsMapper;

    public OrderRepositoryImpl(JdbcTemplateOrderDao orderDao, OrderLineItemRepository itemRepository,
                               MenuRepository menuRepository, OrderMapper orderMapper,
                               OrderLineItemsMapper orderLineItemsMapper) {
        this.orderDao = orderDao;
        this.itemRepository = itemRepository;
        this.menuRepository = menuRepository;
        this.orderMapper = orderMapper;
        this.orderLineItemsMapper = orderLineItemsMapper;
    }

    @Override
    public Order save(Order entity) {
        Order save = orderDao.save(entity);
        List<OrderLineItem> foundItems = findItems(save.getId(), entity);
        return orderMapper.mapOrder(save, itemRepository.saveAll(foundItems));
    }

    private List<OrderLineItem> findItems(Long id, Order entity) {
        return entity.getOrderLineItems().stream()
                .map(orderLineItem -> {
                    Long menuId = menuRepository.findById(orderLineItem.getMenuId()).getId();
                    return orderLineItemsMapper.mapItem(orderLineItem, id, menuId);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Order findById(Long id) {
        Order order = orderDao.findById(id)
                .orElseThrow(() -> new InvalidDataAccessApiUsageException("해당 아이디의 주문은 존재하지 않는다."));
        OrderLineItems orderLineItems = new OrderLineItems(itemRepository.findAllByOrderId(id));
        return orderMapper.mapOrder(order, orderLineItems.getItems());
    }

    @Override
    public List<Order> findAll() {
        return orderDao.findAll().stream()
                .map(order -> orderMapper.mapOrder(order, itemRepository.findAllByOrderId(order.getId())))
                .collect(Collectors.toList());
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
