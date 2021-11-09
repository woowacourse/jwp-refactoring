package kitchenpos.tablegroup.service;

import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupMapper tableGroupMapper;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository,
                             TableGroupMapper tableGroupMapper,
                             TableGroupValidator tableGroupValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupMapper = tableGroupMapper;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        TableGroup tableGroup = tableGroupMapper.mapFrom(tableGroupRequest);
        tableGroup.register();
        tableGroupRepository.save(tableGroup);
        orderTableRepository.saveAll(tableGroup.getGroupTables().toList());
        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
        orderTables.ungroup(tableGroupValidator);
        orderTableRepository.saveAll(orderTables.toList());
    }
}
