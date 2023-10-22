package kitchenpos.order_table.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.order.persistence.OrderDao;
import kitchenpos.order_table.application.entity.OrderTableEntity;
import kitchenpos.order_table.domain.OrderTable;
import kitchenpos.order_table.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderTableRepositoryImpl implements OrderTableRepository {

  private final OrderTableDao orderTableDao;
  private final OrderDao orderDao;

  public OrderTableRepositoryImpl(final OrderTableDao orderTableDao, final OrderDao orderDao) {
    this.orderTableDao = orderTableDao;
    this.orderDao = orderDao;
  }

  @Override
  public OrderTable save(final OrderTable entity) {
    return orderTableDao.save(OrderTableEntity.from(entity)).toOrderTable();
  }

  @Override
  public Optional<OrderTable> findById(final Long id) {
    return orderTableDao.findById(id).map(OrderTableEntity::toOrderTable);
  }

  @Override
  public List<OrderTable> findAll() {
    return orderTableDao.findAll()
        .stream()
        .map(OrderTableEntity::toOrderTable)
        .collect(Collectors.toList());
  }

  @Override
  public List<OrderTable> findAllByIdIn(final List<Long> ids) {
    return orderTableDao.findAllByIdIn(ids)
        .stream()
        .map(OrderTableEntity::toOrderTable)
        .collect(Collectors.toList());
  }

  @Override
  public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
    return orderTableDao.findAllByTableGroupId(tableGroupId)
        .stream()
        .map(OrderTableEntity::toOrderTable)
        .collect(Collectors.toList());
  }

}
