package kitchenpos.application;

import kitchenpos.application.dto.TableGroupRequest;
import kitchenpos.application.dto.TableGroupResponse;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(TableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<TableGroupRequest.OrderTableIdRequest> orderTableRequests = request.getOrderTables();

        final List<Long> orderTableIds = orderTableRequests.stream()
                .map(TableGroupRequest.OrderTableIdRequest::getId)
                .collect(Collectors.toList());
        TableGroup entity = new TableGroup(LocalDateTime.now());
        TableGroup tableGroup = tableGroupRepository.save(entity.group(orderTableIds));
        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("테이블 그룹이 없습니다"));
        tableGroup.unGroup();
        tableGroupRepository.save(tableGroup);
    }
}
