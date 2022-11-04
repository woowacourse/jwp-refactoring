package kitchenpos.table.repository;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.table.repository.jdbc.JdbcTemplateOrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

@Repository
public class OrderTableRepositoryImpl implements OrderTableRepository {

    private final JdbcTemplateOrderTableDao tableDao;

    public OrderTableRepositoryImpl(JdbcTemplateOrderTableDao tableDao) {
        this.tableDao = tableDao;
    }


    @Override
    public OrderTable save(OrderTable entity) {
        return tableDao.save(entity);
    }

    public List<OrderTable> saveAll(List<OrderTable> tables) {
        List<OrderTable> orderTables = new ArrayList<>();
        for (final OrderTable table : tables) {
            orderTables.add(tableDao.save(table));
        }
        return orderTables;
    }

    @Override
    public OrderTable findById(Long id) {
        return tableDao.findById(id)
                .orElseThrow(() -> new InvalidDataAccessApiUsageException("테이블은 존재해야 한다."));
    }

    @Override
    public List<OrderTable> findAll() {
        return tableDao.findAll();
    }

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        return tableDao.findAllByIdIn(ids);
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return tableDao.findAllByTableGroupId(tableGroupId);
    }
}
