package kitchenpos.tablegroup.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderTableIdRequest;
import kitchenpos.application.dto.request.TableGroupRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<Long> orderTableIds = convertToLong(request.getOrderTables());
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateInvalidOrderTable(orderTableIds, savedOrderTables);

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);
        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    private List<Long> convertToLong(List<OrderTableIdRequest> requests) {
        return requests.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }

    private void validateInvalidOrderTable(final List<Long> orderTableIds, final List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블 또는 중복 테이블이 포함되어 있습니다.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).
                orElseThrow(() -> new IllegalArgumentException("단체 지정 내역을 찾을 수 없습니다."));
        tableGroup.ungroup();
    }
}
