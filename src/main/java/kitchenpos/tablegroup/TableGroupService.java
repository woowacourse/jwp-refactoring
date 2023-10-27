package kitchenpos.tablegroup;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.ordertable.JpaOrderTableRepository;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTableEvent;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final JpaOrderTableRepository jpaOrderTableRepository;
    private final JpaTableGroupRepository jpaTableGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableGroupService(
            JpaOrderTableRepository jpaOrderTableRepository,
            JpaTableGroupRepository jpaTableGroupRepository,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        this.jpaOrderTableRepository = jpaOrderTableRepository;
        this.jpaTableGroupRepository = jpaTableGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {

        List<Long> orderTableIds = request.getOrderTableIds().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = jpaOrderTableRepository.findAllByIdIn(orderTableIds);

        if (savedOrderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException();
        }

        TableGroup tableGroup = new TableGroup();
        final TableGroup savedTableGroup = jpaTableGroupRepository.save(tableGroup);
        savedTableGroup.setOrderTables(savedOrderTables);
        return new TableGroupResponse(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = jpaTableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        final List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        for (Long orderTableId : orderTableIds) {
            applicationEventPublisher.publishEvent(new OrderTableEvent(orderTableId));
        }

        tableGroup.ungroup();
    }
}
