package kitchenpos.application;

import kitchenpos.application.request.TableIdRequest;
import kitchenpos.tablegroup.GroupManager;
import kitchenpos.tablegroup.TableGroup;
import kitchenpos.tablegroup.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final GroupManager groupManager;

    public TableGroupService(
            TableGroupRepository tableGroupRepository,
            GroupManager groupManager
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.groupManager = groupManager;
    }

    @Transactional
    public TableGroup create(List<TableIdRequest> tableIds) {
        List<Long> ids = tableIds.stream()
                .map(TableIdRequest::getId)
                .collect(Collectors.toList());

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());

        groupManager.group(ids, tableGroup.getId());

        return tableGroup;
    }

    @Transactional
    public TableGroup ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("테이블 그룹이 존재하지 않습니다."));

        groupManager.unGroup(tableGroupId);

        return tableGroup;
    }
}
