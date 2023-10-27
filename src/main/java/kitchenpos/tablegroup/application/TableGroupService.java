package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.domain.GroupingService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
import kitchenpos.tablegroup.domain.validator.TableGroupValidator;
import kitchenpos.tablegroup.ui.request.TableGroupRequest;
import kitchenpos.tablegroup.ui.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;
    private final GroupingService groupingService;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
                             final TableGroupValidator tableGroupValidator,
                             final GroupingService groupingService) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
        this.groupingService = groupingService;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        final TableGroup tableGroup = TableGroup.create(
                orderTableIds,
                tableGroupValidator);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        groupingService.group(orderTableIds, savedTableGroup.getId());
        return TableGroupResponse.from(savedTableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
        tableGroup.ungroup(groupingService, tableGroupValidator);
    }
}
