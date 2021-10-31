package kitchenpos.application;

import kitchenpos.application.dto.request.TableGroupRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.application.mapper.TableGroupMapper;
import kitchenpos.domain.GroupTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.domain.validator.TableGroupValidator;
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
        GroupTables groupTables = new GroupTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
        groupTables.ungroup(tableGroupValidator);
        orderTableRepository.saveAll(groupTables.toList());
    }
}
