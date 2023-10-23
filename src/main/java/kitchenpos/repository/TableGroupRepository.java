package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.JdbcTemplateTableGroupDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepository implements TableGroupDao {

    private JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    public TableGroupRepository(final JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao) {
        this.jdbcTemplateTableGroupDao = jdbcTemplateTableGroupDao;
    }

    @Override
    public TableGroup save(final TableGroup entity) {
        return jdbcTemplateTableGroupDao.save(entity);
    }

    @Override
    public Optional<TableGroup> findById(final Long id) {
        return jdbcTemplateTableGroupDao.findById(id);
    }

    @Override
    public List<TableGroup> findAll() {
        return jdbcTemplateTableGroupDao.findAll();
    }
}
