package kitchenpos.application;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.GroupTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.domain.TableOrderEmptyValidateService;
import kitchenpos.domain.TableRepository;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;

@Service
public class TableGroupService {
    private final TableRepository tableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableOrderEmptyValidateService tableOrderEmptyValidateService;

    public TableGroupService(TableRepository tableRepository, TableGroupRepository tableGroupRepository,
        TableOrderEmptyValidateService tableOrderEmptyValidateService) {
        this.tableRepository = tableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableOrderEmptyValidateService = tableOrderEmptyValidateService;
    }

    @Transactional
    public TableGroupResponse create(TableGroupCreateRequest tableGroupCreateRequest) {
        GroupTables tables = createGroupTables(tableGroupCreateRequest.toTableIds());
        TableGroup tableGroup = tableGroupRepository.save(TableGroup.create());

        tables.designateGroup(tableGroup.getId());

        return TableGroupResponse.of(tableGroup, tables);
    }

    private GroupTables createGroupTables(Set<Long> tableIds) {
        GroupTables tables = new GroupTables(tableRepository.findAllById(tableIds));
        if (!tables.hasSize(tableIds.size())) {
            throw new IllegalArgumentException();
        }
        return tables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        GroupTables groupTables = new GroupTables(tableRepository.findByTableGroupId(tableGroupId));
        groupTables.ungroup(tableOrderEmptyValidateService);
    }
}
