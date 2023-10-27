package kitchenpos.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.CreateTableGroupRequest;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.event.GroupOrderTablesEvent;
import kitchenpos.event.UngroupOrderTablesEvent;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.exception.TableGroupNotFoundException;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final ApplicationEventPublisher eventPublisher;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(ApplicationEventPublisher eventPublisher,
            OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.eventPublisher = eventPublisher;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(CreateTableGroupRequest request) {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        List<OrderTable> orderTables = findOrderTables(request.getOrderTables());
        tableGroupRepository.save(tableGroup);
        publishGroupOrderTablesEvent(orderTables, tableGroup);

        return TableGroupResponse.from(tableGroup, orderTables);
    }

    private List<OrderTable> findOrderTables(List<OrderTableRequest> orderTableRequests) {
        return orderTableRequests.stream()
                .map(each -> orderTableRepository.findById(each.getId())
                        .orElseThrow(OrderTableNotFoundException::new))
                .collect(Collectors.toList());
    }

    private void publishGroupOrderTablesEvent(List<OrderTable> orderTables, TableGroup tableGroup) {
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        eventPublisher.publishEvent(new GroupOrderTablesEvent(tableGroup.getId(), orderTableIds));
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(TableGroupNotFoundException::new);

        eventPublisher.publishEvent(new UngroupOrderTablesEvent(tableGroup.getId()));
    }
}
