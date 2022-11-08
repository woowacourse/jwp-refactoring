package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTableValidator;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.request.TableGroupRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableGroupMapper tableGroupMapper;
    private final OrderTableValidator orderTableValidator;

    public TableGroupService(final TableGroupRepository tableGroupRepository, final TableGroupMapper tableGroupMapper,
                             final OrderTableValidator orderTableValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupMapper = tableGroupMapper;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        TableGroup tableGroup = tableGroupMapper.from(request);
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return new TableGroupResponse(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        tableGroup.ungroup(orderTableValidator);
    }
}
