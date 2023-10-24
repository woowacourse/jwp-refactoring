package kitchenpos.tablegroup.application;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.common.dto.response.TableResponse;
import kitchenpos.tablegroup.domain.GroupedTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.common.dto.request.TableGroupCreationRequest;
import kitchenpos.common.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository,
            TableGroupValidator tableGroupValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(TableGroupCreationRequest request) {
        TableGroup tableGroup = TableGroup.create();
        tableGroupRepository.save(tableGroup);

        GroupedTables groupedTables = createGroupedOrderTable(request.getOrderTableIds());
        groupedTables.group(tableGroup.getId());

        return mapToTableGroupResponse(tableGroup, groupedTables);
    }

    private GroupedTables createGroupedOrderTable(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        return GroupedTables.createForGrouping(orderTables);
    }

    private TableGroupResponse mapToTableGroupResponse(TableGroup tableGroup, GroupedTables groupedTables) {
        List<TableResponse> tableResponses = TableResponse.from(groupedTables.getOrderTables());

        return TableGroupResponse.from(tableGroup, tableResponses);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        GroupedTables groupedGroupedTables = GroupedTables.createFourUngrouping(orderTables);

        groupedGroupedTables.ungroup(tableGroupValidator);
    }

}
