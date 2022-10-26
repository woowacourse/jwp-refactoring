package kitchenpos.dao.repository;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.dao.jdbctemplate.JdbcTemplateTableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepository implements TableGroupDao {

    private final JdbcTemplateTableGroupDao tableGroupDao;
    private final TableRepository tableRepository;


    public TableGroupRepository(JdbcTemplateTableGroupDao tableGroupDao,
                                TableRepository tableRepository) {
        this.tableGroupDao = tableGroupDao;
        this.tableRepository = tableRepository;
    }

    @Override
    public TableGroup save(TableGroup entity) {
        TableGroup save = tableGroupDao.save(entity);

        List<OrderTable> orderTables = entity.getOrderTables().stream()
                .map(orderTable -> getTable(save.getId(), orderTable))
                .collect(Collectors.toList());
        save.placeOrderTables(orderTables);
        return save;
    }

    private OrderTable getTable(Long id, OrderTable orderTable) {
        OrderTable table = tableRepository.findById(orderTable.getId());
        table.placeTableGroupId(id);
        return table;
    }

    @Override
    public TableGroup findById(Long id) {
        TableGroup tableGroup = tableGroupDao.findById(id)
                .orElseThrow(() -> new InvalidDataAccessApiUsageException("단체 지정은 존재해야 한다."));
        tableGroup.placeOrderTables(tableRepository.findAllByTableGroupId(id));
        return tableGroup;
    }

    @Override
    public List<TableGroup> findAll() {
        return tableGroupDao.findAll();
    }
}
