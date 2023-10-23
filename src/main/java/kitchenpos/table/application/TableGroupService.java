package kitchenpos.table.application;

import java.time.LocalDateTime;
import kitchenpos.table.application.dto.TableGroupResult;
import kitchenpos.table.application.dto.TableGroupingRequest;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.OrderTablesGroupingValidator;
import kitchenpos.table.domain.OrderTablesValidator;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTablesMapper orderTablesMapper;
    private final OrderTablesValidator orderTablesGroupingValidator;
    private final OrderTablesValidator ordersStatusValidator;

    public TableGroupService(
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository,
            final OrderTablesMapper orderTablesMapper,
            final OrderTablesGroupingValidator orderTablesGroupingValidator,
            final OrderTablesValidator ordersStatusValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderTablesGroupingValidator = orderTablesGroupingValidator;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTablesMapper = orderTablesMapper;
        this.ordersStatusValidator = ordersStatusValidator;
    }

    @Transactional
    public TableGroupResult create(final TableGroupingRequest request) {
        final OrderTables orderTables = orderTablesMapper.from(request);
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        orderTables.groupByTableGroup(tableGroup, orderTablesGroupingValidator);
        orderTableRepository.saveAll(orderTables.getOrderTables());
        return TableGroupResult.from(tableGroup);
    }

    @Transactional
    public void ungroup(final Long ungroupTableId) {
        final OrderTables orderTables = orderTablesMapper.fromTable(ungroupTableId);
        orderTables.ungroup(ordersStatusValidator);
    }
}
