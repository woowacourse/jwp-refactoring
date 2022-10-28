package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Repository;

@Repository
public class OrderTableRepository {

    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public OrderTableRepository(final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    public OrderTable save(final OrderTable orderTable) {
        return orderTableDao.save(orderTable);
    }

    public void saveAll(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            save(orderTable);
        }
    }

    public TableGroup saveTableGroup(final TableGroup tableGroup) {
        return tableGroupDao.save(tableGroup);
    }

    public Optional<OrderTable> findById(final Long id) {
        return orderTableDao.findById(id);
    }

    public List<OrderTable> findGroupedTables(final Long tableGroupId) {
        return orderTableDao.findAllByTableGroupId(tableGroupId);
    }
}
