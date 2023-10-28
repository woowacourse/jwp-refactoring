package kitchenpos.order.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableValidator;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.order.domain.repository.TableGroupRepository;
import kitchenpos.order.service.dto.TableGroupRequest;
import kitchenpos.order.service.dto.TableGroupResponse;
import kitchenpos.order.service.dto.TableIdRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<Long> orderTableIds = convertToLong(request.getOrderTables());
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateInvalidOrderTable(orderTableIds, savedOrderTables);

        final TableGroup tableGroup = new TableGroup(orderTableIds.size());
        tableGroupRepository.save(tableGroup);
        for (OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.groupBy(tableGroup.getId());
        }
        return TableGroupResponse.from(tableGroup, savedOrderTables);
    }

    private List<Long> convertToLong(List<TableIdRequest> requests) {
        return requests.stream()
                .map(TableIdRequest::getId)
                .collect(Collectors.toList());
    }

    private void validateInvalidOrderTable(final List<Long> orderTableIds, final List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블 또는 중복 테이블이 포함되어 있습니다.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findTableGroupById(tableGroupId);
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        for (OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.ungroupBy(tableGroup.getId(), orderTableValidator);
        }
    }

    private TableGroup findTableGroupById(final Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId).
                orElseThrow(() -> new IllegalArgumentException("단체 지정 내역을 찾을 수 없습니다."));
    }
}
