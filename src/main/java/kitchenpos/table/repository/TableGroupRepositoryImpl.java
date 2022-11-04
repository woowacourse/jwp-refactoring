package kitchenpos.table.repository;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.repository.jdbc.JdbcTemplateTableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.Tables;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepositoryImpl implements TableGroupRepository {

    private final JdbcTemplateTableGroupDao tableGroupDao;
    private final OrderTableRepository orderTableRepository;


    public TableGroupRepositoryImpl(JdbcTemplateTableGroupDao tableGroupDao,
                                    OrderTableRepository orderTableRepository) {
        this.tableGroupDao = tableGroupDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public TableGroup save(TableGroup entity) {
        TableGroup save = tableGroupDao.save(entity);

        List<OrderTable> orderTables = entity.getOrderTableValues().stream()
                .map(orderTable -> orderTableRepository.findById(orderTable.getId()))
                .collect(Collectors.toList());
        save.placeOrderTables(new Tables(orderTables));
        save.placeTableGroupId();
        for (final OrderTable orderTable : orderTables) {
            orderTableRepository.save(orderTable);
        }
        return save;
    }

    @Override
    public TableGroup findById(Long id) {
        TableGroup tableGroup = tableGroupDao.findById(id)
                .orElseThrow(() -> new InvalidDataAccessApiUsageException("단체 지정은 존재해야 한다."));
        Tables orderTables = new Tables(orderTableRepository.findAllByTableGroupId(id));
        tableGroup.placeOrderTables(orderTables);
        return tableGroup;
    }

    @Override
    public List<TableGroup> findAll() {
        return tableGroupDao.findAll();
    }
}
