package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.MenuEntity;
import kitchenpos.dao.entity.OrderEntity;
import kitchenpos.dao.entity.OrderLineItemEntity;
import kitchenpos.dao.entity.OrderTableEntity;
import kitchenpos.dao.entity.TableGroupEntity;
import kitchenpos.domain.Menu2;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.Order2;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItem2;
import kitchenpos.domain.OrderLineItemRepository;
import kitchenpos.domain.OrderTable2;
import kitchenpos.domain.TableGroup2;
import org.springframework.stereotype.Repository;

@Repository
public class OrderLineItemRepositoryImpl implements OrderLineItemRepository {

  private final OrderLineItemDao2 orderLineItemDao;
  private final MenuGroupDao2 menuGroupDao;
  private final OrderDao2 orderDao;
  private final OrderTableDao2 orderTableDao;
  private final TableGroupDao2 tableGroupDao;
  private final MenuDao2 menuDao;

  public OrderLineItemRepositoryImpl(
      final OrderLineItemDao2 orderLineItemDao,
      final MenuGroupDao2 menuGroupDao,
      final OrderDao2 orderDao,
      final OrderTableDao2 orderTableDao,
      final TableGroupDao2 tableGroupDao,
      final MenuDao2 menuDao
  ) {
    this.orderLineItemDao = orderLineItemDao;
    this.menuGroupDao = menuGroupDao;
    this.orderDao = orderDao;
    this.orderTableDao = orderTableDao;
    this.tableGroupDao = tableGroupDao;
    this.menuDao = menuDao;
  }

  @Override
  public OrderLineItem2 save(final OrderLineItem2 orderLineItem, final Order2 order) {
    final OrderLineItemEntity orderLineItemEntity = new OrderLineItemEntity(
        order.getId(),
        orderLineItem.getMenu().getId(),
        orderLineItem.getQuantity()
    );

    return mapToOrderLineItem(orderLineItemDao.save(orderLineItemEntity));
  }

  private OrderLineItem2 mapToOrderLineItem(final OrderLineItemEntity orderLineItemEntity) {
    return new OrderLineItem2(
        orderLineItemEntity.getSeq(),
        menuDao.findById(orderLineItemEntity.getMenuId())
            .map(this::mapToMenu)
            .orElseThrow(IllegalArgumentException::new),
        orderLineItemEntity.getQuantity()
    );
  }

  private OrderTable2 mapToOrderTable(final OrderTableEntity orderTableEntity) {
    return new OrderTable2(
        orderTableEntity.getId(),
        mapToTableGroup(orderTableEntity),
        orderTableEntity.getNumberOfGuests(),
        orderTableEntity.isEmpty()
    );
  }

  private TableGroup2 mapToTableGroup(final OrderTableEntity orderTableEntity) {
    return tableGroupDao.findById(orderTableEntity.getTableGroupId())
        .map(tableGroupEntity -> new TableGroup2(
            tableGroupEntity.getId(),
            tableGroupEntity.getCreatedDate()
        ))
        .orElseThrow(IllegalArgumentException::new);
  }

  private Menu2 mapToMenu(final MenuEntity menuEntity) {
    return new Menu2(
        menuEntity.getId(),
        menuEntity.getName(),
        menuEntity.getPrice(),
        mapToMenuGroup(menuEntity)
    );
  }

  private MenuGroup mapToMenuGroup(final MenuEntity menuEntity) {
    return menuGroupDao.findById(menuEntity.getMenuGroupId())
        .map(menuGroupEntity -> new MenuGroup(menuGroupEntity.getId(), menuGroupEntity.getName()))
        .orElseThrow(IllegalArgumentException::new);
  }

  @Override
  public Optional<OrderLineItem2> findById(final Long id) {
    return orderLineItemDao.findById(id)
        .map(this::mapToOrderLineItem);
  }

  @Override
  public List<OrderLineItem2> findAll() {
    return orderLineItemDao.findAll()
        .stream()
        .map(this::mapToOrderLineItem)
        .collect(Collectors.toList());
  }

  @Override
  public List<OrderLineItem2> findAllByOrderId(final Long orderId) {
    return orderLineItemDao.findAllByOrderId(orderId)
        .stream()
        .map(this::mapToOrderLineItem)
        .collect(Collectors.toList());
  }
}
