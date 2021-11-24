package kitchenpos.menu.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.OrderTable;
import kitchenpos.menu.domain.OrderTableRepository;
import kitchenpos.menu.domain.TableGroup;
import kitchenpos.menu.domain.TableGroupRepository;
import kitchenpos.menu.dto.TableGroupRequest;
import kitchenpos.menu.dto.TableGroupRequest.OrderTableOfGroupRequest;
import kitchenpos.menu.dto.TableGroupResponse;
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
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = getOrderTableIds(tableGroupRequest);
        final List<OrderTable> savedOrderTables = findAllOrderTables(orderTableIds);
        final TableGroup tableGroup = new TableGroup(savedOrderTables);

        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    private List<Long> getOrderTableIds(final TableGroupRequest tableGroupRequest) {
        List<OrderTableOfGroupRequest> orderTables = tableGroupRequest.getOrderTables();
        if (Objects.isNull(orderTables)) {
            throw new IllegalArgumentException();
        }

        return orderTables.stream()
            .map(OrderTableOfGroupRequest::getId)
            .collect(Collectors.toList());
    }

    private List<OrderTable> findAllOrderTables(final List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException();
        }
        return orderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("존재하지 않는 ID 입니다. (id: %d)", tableGroupId)
            ));
        tableGroup.removeAllOrderTables();
    }
}
