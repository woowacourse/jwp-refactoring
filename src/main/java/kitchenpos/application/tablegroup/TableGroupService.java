package kitchenpos.application.tablegroup;

import kitchenpos.application.dto.TableGroupRequest;
import kitchenpos.application.dto.TableGroupResponse;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.domain.tablegroup.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.application.dto.TableGroupRequest.OrderTableIdRequest;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(TableGroupRepository tableGroupRepository, TableGroupValidator tableGroupValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTableIdRequest> orderTableRequests = request.getOrderTables();

        // TODO : 여기 맘에 안듬
        final List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
        tableGroupValidator.validateGroup(orderTableIds);
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
