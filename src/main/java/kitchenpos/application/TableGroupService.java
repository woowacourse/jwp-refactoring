package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.request.TableGroupIdRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(TableGroupCreateRequest request) {
        List<OrderTable> savedOrderTables = findOrderTables(request);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);
        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    private List<OrderTable> findOrderTables(TableGroupCreateRequest request) {
        List<Long> orderTableIds = request.getOrderTableIds().stream()
                .map(TableGroupIdRequest::getId)
                .collect(Collectors.toList());
        return orderTableRepository.getAllByIdIn(orderTableIds);
    }

    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.getById(tableGroupId);
        tableGroup.ungroupOrderTables();
    }
}
