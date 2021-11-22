package table.application;

import java.util.List;
import java.util.stream.Collectors;
import table.domain.OrderTable;
import table.domain.OrdersValidatedEvent;
import table.domain.TableGroup;
import table.domain.Tables;
import table.domain.repository.OrderTableRepository;
import table.domain.repository.TableGroupRepository;
import table.dto.request.OrderTableRequest;
import table.dto.request.TableGroupRequest;
import table.dto.response.TableGroupResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final ApplicationEventPublisher applicationEventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTableRequest> orderTables = tableGroupRequest.getOrderTableRequests();

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<Long> orderTableIds = getOrderTableIds(orderTables);

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIsIn(orderTableIds);
        Tables tables = new Tables(orderTableRepository.findAllByIdIsIn(orderTableIds));
        tables.validateSize(orderTables.size());
        tables.validateCondition();

        TableGroup tableGroup = new TableGroup();
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        tables.changeCondition(tableGroup.getId());

        return TableGroupResponse.of(savedTableGroup, savedOrderTables);
    }

    private List<Long> getOrderTableIds(List<OrderTableRequest> orderTables) {
        return orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        for (Long orderTableId : orderTableIds) {
            applicationEventPublisher.publishEvent(new OrdersValidatedEvent(orderTableId));
        }
        Tables tables = new Tables(orderTables);
        tables.changeCondition(null);
    }
}
