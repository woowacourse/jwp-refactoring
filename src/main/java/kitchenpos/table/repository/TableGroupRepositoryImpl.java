package kitchenpos.table.repository;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.Tables;
import kitchenpos.table.repository.jdbc.JdbcTemplateTableGroupDao;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

@Repository
public class TableGroupRepositoryImpl implements TableGroupRepository {

    private final JdbcTemplateTableGroupDao tableGroupDao;
    private final OrderTableRepository orderTableRepository;
    private final TableMapper tableMapper;


    public TableGroupRepositoryImpl(JdbcTemplateTableGroupDao tableGroupDao,
                                    OrderTableRepository orderTableRepository,
                                    TableMapper tableMapper) {
        this.tableGroupDao = tableGroupDao;
        this.orderTableRepository = orderTableRepository;
        this.tableMapper = tableMapper;
    }

    @Override
    public TableGroup save(TableGroup entity) {
        TableGroup save = tableGroupDao.save(entity);
        List<OrderTable> foundOrderTables = findOrderTables(entity);
        TableGroup tableGroup = tableMapper.mapTable(save, foundOrderTables);

        return tableMapper.mapTable(tableGroup, orderTableRepository.saveAll(tableGroup.getOrderTableValues()));
    }

    private List<OrderTable> findOrderTables(TableGroup entity) {
        return entity.getOrderTableValues().stream()
                .map(orderTable -> orderTableRepository.findById(orderTable.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public TableGroup findById(Long id) {
        TableGroup tableGroup = tableGroupDao.findById(id)
                .orElseThrow(() -> new InvalidDataAccessApiUsageException("단체 지정은 존재해야 한다."));
        Tables orderTables = new Tables(orderTableRepository.findAllByTableGroupId(id));
        return tableMapper.mapTable(tableGroup, orderTables.getValue());
    }

    @Override
    public List<TableGroup> findAll() {
        return tableGroupDao.findAll();
    }
}
