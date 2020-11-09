package kitchenpos.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.command.CreateTableGroupCommand;
import kitchenpos.application.response.TableGroupResponse;
import kitchenpos.domain.model.tablegroup.TableGroup;
import kitchenpos.domain.model.tablegroup.TableGroupCreateService;
import kitchenpos.domain.model.tablegroup.TableGroupRepository;
import kitchenpos.domain.model.tablegroup.TableGroupUngroupService;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupCreateService tableGroupCreateService;
    private final TableGroupUngroupService tableGroupUngroupService;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
            TableGroupCreateService tableGroupCreateService,
            TableGroupUngroupService tableGroupUngroupService) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupCreateService = tableGroupCreateService;
        this.tableGroupUngroupService = tableGroupUngroupService;
    }

    @Transactional
    public TableGroupResponse create(final CreateTableGroupCommand command) {
        TableGroup tableGroup = command.toEntity();
        TableGroup saved = tableGroupRepository.save(tableGroup.create(tableGroupCreateService));
        return TableGroupResponse.of(saved);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
        tableGroup.ungroup(tableGroupUngroupService);
    }
}
