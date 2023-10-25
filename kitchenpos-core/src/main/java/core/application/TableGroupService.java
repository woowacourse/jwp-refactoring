package core.application;

import core.application.dto.TableGroupRequest;
import core.application.dto.TableGroupResponse;
import core.domain.TableGroup;
import core.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static core.application.dto.TableGroupRequest.OrderTableIdRequest;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(TableGroupRepository tableGroupRepository) {
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTableIdRequest> orderTableRequests = request.getOrderTables();

        final List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableIdRequest::getId)
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
