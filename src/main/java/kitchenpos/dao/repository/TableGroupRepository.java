package kitchenpos.dao.repository;

import java.util.List;
import kitchenpos.dao.jdbctemplate.JdbcTemplateTableGroupDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepository implements TableGroupDao {

    private final JdbcTemplateTableGroupDao tableGroupDao;


    public TableGroupRepository(JdbcTemplateTableGroupDao tableGroupDao) {
        this.tableGroupDao = tableGroupDao;
    }

    @Override
    public TableGroup save(TableGroup entity) {
        return tableGroupDao.save(entity);
    }

    @Override
    public TableGroup findById(Long id) {
        return tableGroupDao.findById(id)
                .orElseThrow(() -> new InvalidDataAccessApiUsageException("단체 지정은 존재해야 한다."));
    }

    @Override
    public List<TableGroup> findAll() {
        return tableGroupDao.findAll();
    }
}
