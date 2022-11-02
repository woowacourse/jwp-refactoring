package kitchenpos.table.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.request.GroupedTableCreateRequest;
import kitchenpos.table.dto.request.TableGroupCreateRequest;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableGroupService(OrderTableRepository orderTableRepository,
        TableGroupRepository tableGroupRepository,
        ApplicationEventPublisher applicationEventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public TableGroup create(final TableGroupCreateRequest request) {
        List<GroupedTableCreateRequest> orderTableRequests = request.getOrderTables();

        List<OrderTable> orderTables = extractOrderTables(orderTableRequests);
        TableGroup tableGroup = new TableGroup(orderTables);

        return tableGroupRepository.save(tableGroup);
    }

    private List<OrderTable> extractOrderTables(List<GroupedTableCreateRequest> requests) {
        List<OrderTable> orderTables = new ArrayList<>();

        for (GroupedTableCreateRequest request : requests) {
            OrderTable orderTable = orderTableRepository.findById(request.getId())
                .orElseThrow(IllegalArgumentException::new);
            orderTables.add(orderTable);
        }

        return orderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(IllegalArgumentException::new);

        List<OrderTable> orderTables = tableGroup.getOrderTables();

        applicationEventPublisher.publishEvent(new UngroupedEvent(orderTables));

        tableGroup.ungroup();
    }
}
