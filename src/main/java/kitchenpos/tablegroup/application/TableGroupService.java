package kitchenpos.tablegroup.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.application.OrderTablesMapper;
import kitchenpos.table.application.dto.GroupOrderTableRequest;
import kitchenpos.table.application.dto.TableGroupResult;
import kitchenpos.table.application.dto.TableGroupingRequest;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.OrderTablesValidator;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroupingEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTablesMapper orderTablesMapper;
    private final OrderTablesValidator ordersStatusValidator;
    private final ApplicationEventPublisher eventPublisher;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final OrderTablesMapper orderTablesMapper,
            final OrderTablesValidator ordersStatusValidator,
            final ApplicationEventPublisher eventPublisher
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTablesMapper = orderTablesMapper;
        this.ordersStatusValidator = ordersStatusValidator;
        this.eventPublisher = eventPublisher;
    }

    public TableGroupResult create(final TableGroupingRequest request) {
        final List<Long> orderTableIds = extractTableIdsFrom(request);
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        eventPublisher.publishEvent(new TableGroupingEvent(tableGroup.getId(), orderTableIds));
        return TableGroupResult.from(tableGroup);
    }

    private List<Long> extractTableIdsFrom(final TableGroupingRequest request) {
        return request.getOrderTables().stream()
                .map(GroupOrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    public void ungroup(final Long ungroupTableId) {
        final OrderTables orderTables = orderTablesMapper.fromTable(ungroupTableId);
        orderTables.ungroup(ordersStatusValidator);
    }
}
