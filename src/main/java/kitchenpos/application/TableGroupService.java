package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.tablegroup.TableGroupRequest;
import kitchenpos.application.dto.tablegroup.TableGroupResponse;
import kitchenpos.application.dto.tablegroup.TableOfGroupDto;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupManager;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableGroupManager tableGroupManager;

    public TableGroupService(final TableGroupRepository tableGroupRepository, final TableGroupManager tableGroupManager) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupManager = tableGroupManager;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        final List<Long> tableIdsToGroup = convertToTableIds(tableGroupRequest.getOrderTables());
        tableGroupManager.group(tableGroup, tableIdsToGroup);
        tableGroupRepository.save(tableGroup);

        return TableGroupResponse.of(tableGroup, tableIdsToGroup);
    }

    private List<Long> convertToTableIds(final List<TableOfGroupDto> tables) {
        return tables.stream()
            .map(TableOfGroupDto::getId)
            .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블 그룹입니다."));

       tableGroupManager.ungroup(tableGroup);
    }
}
