package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.event.VerifiedAbleToUngroupEvent;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.request.CreateTableGroupRequest;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher publisher;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository,
        ApplicationEventPublisher publisher) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.publisher = publisher;
    }

    @Transactional
    public TableGroup create(final CreateTableGroupRequest request) {
        return tableGroupRepository.save(
            new TableGroup(getOrderTables(request))
        );
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findByTableGroupId(tableGroupId);
        publisher.publishEvent(new VerifiedAbleToUngroupEvent(orderTables));

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    private List<OrderTable> getOrderTables(final CreateTableGroupRequest request) {
        final List<Long> orderTableIds = request.getOrderTables().stream()
            .map(it -> it.getId())
            .collect(Collectors.toList());

        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != orderTables.size()) {
            new IllegalArgumentException("존재하지 않은 테이블입니다.");
        }

        return orderTables;
    }

}
