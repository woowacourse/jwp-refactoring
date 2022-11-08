package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.domain.collection.OrderTables;
import kitchenpos.table.domain.entity.TableGroup;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import kitchenpos.table.ui.dto.tablegroup.TableGroupCreateRequest;
import kitchenpos.table.ui.dto.tablegroup.TableGroupCreateResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private TableGroupRepository tableGroupRepository;
    private OrderTableRepository orderTableRepository;
    private TableServiceAssistant tableServiceAssistant;
    private TableGroupRule tableGroupRule;

    public TableGroupService(TableGroupRepository tableGroupRepository,
                             OrderTableRepository orderTableRepository,
                             TableServiceAssistant tableServiceAssistant,
                             TableGroupRule tableGroupRule) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableServiceAssistant = tableServiceAssistant;
        this.tableGroupRule = tableGroupRule;
    }

    @Transactional
    public TableGroupCreateResponse create(TableGroupCreateRequest tableGroupCreateRequest) {
        OrderTables orderTables = tableServiceAssistant.findOrderTables(tableGroupCreateRequest.getOrderTableIds());
        List<Long> orderTableIds = orderTables.getOrderTableIds();
        OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllById(orderTableIds));
        if (!savedOrderTables.isAbleToGroup()) {
            throw new IllegalArgumentException();
        }

        if (!savedOrderTables.isReadyToGroup(orderTables)) {
            throw new IllegalArgumentException();
        }

        TableGroup tableGroup = new TableGroup();
        tableGroupRepository.save(tableGroup);
        savedOrderTables.group(tableGroup);


        return new TableGroupCreateResponse(tableGroup.getId(), tableGroup.getCreatedDate(), savedOrderTables.getOrderTableIds());
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        OrderTables orderTables = tableServiceAssistant.findTablesInGroup(tableGroupId);
        if (tableGroupRule.unableToUngroup(orderTables)) {
            throw new IllegalStateException();
        }

        orderTables.ungroup();
    }
}
