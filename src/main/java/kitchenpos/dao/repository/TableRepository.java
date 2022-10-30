package kitchenpos.dao.repository;

import java.util.List;
import kitchenpos.dao.jdbctemplate.JdbcTemplateOrderTableDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

@Repository
public class TableRepository implements OrderTableDao {

    private final JdbcTemplateOrderTableDao tableDao;

    public TableRepository(JdbcTemplateOrderTableDao tableDao) {
        this.tableDao = tableDao;
    }


    @Override
    public OrderTable save(OrderTable entity) {
        return tableDao.save(entity);
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
