package kitchenpos.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.application.dto.TableGroupResponse;
import kitchenpos.domain.entity.TableGroup;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.domain.service.TableGroupCreateService;
import kitchenpos.domain.service.TableGroupUngroupService;

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
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        TableGroup tableGroup = request.toEntity();
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
