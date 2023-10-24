package kitchenpos.domain.tablegroup;

import kitchenpos.application.dto.request.CreateTableGroupRequest;
import kitchenpos.application.dto.response.CreateTableGroupResponse;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupMapper tableGroupMapper;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(TableGroupMapper tableGroupMapper,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             TableGroupValidator tableGroupValidator) {
        this.tableGroupMapper = tableGroupMapper;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public CreateTableGroupResponse create(final CreateTableGroupRequest request) {
        final TableGroup tableGroup = tableGroupMapper.toTableGroup(request);
        TableGroup updated = tableGroup.fillTables();
        return CreateTableGroupResponse.from(tableGroupRepository.save(updated));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
        tableGroupValidator.validateUngroup(tableGroup);
        tableGroup.getOrderTables().stream()
                .map(OrderTable::ungroup)
                .forEach(orderTableRepository::save);
    }
}
