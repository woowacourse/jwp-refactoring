package kitchenpos.table.persistence;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.persistence.entity.OrderTableEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderTableRepositoryImpl implements OrderTableRepository {

    private final OrderTableDao orderTableDao;

    public OrderTableRepositoryImpl(final OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
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
