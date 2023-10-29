package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.table.application.dto.event.TableGroupCreateEvent;
import kitchenpos.table.application.dto.event.TableGroupUnGroupEvent;
import kitchenpos.table.application.dto.request.OrderTableRequest;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.application.dto.request.TableGroupCreateRequest;
import kitchenpos.table.application.dto.response.TableGroupResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
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
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        final List<OrderTable> orderTables = findAllOrderTableByIdIn(extractOrderTableIds(request.getOrderTables()));
        applicationEventPublisher.publishEvent(new TableGroupCreateEvent(orderTables));
        OrderTables.from(orderTables)
                .group(tableGroup);
        return TableGroupResponse.of(tableGroupRepository.save(tableGroup), orderTables);
    }

    private List<Long> extractOrderTableIds(List<OrderTableRequest> orderTableRequests) {
        return orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    private List<OrderTable> findAllOrderTableByIdIn(List<Long> orderTableIds) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("잘못된 테이블 정보가 포함되어 있습니다.");
        }
        return orderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.getByIdOrThrow(tableGroupId);
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        applicationEventPublisher.publishEvent(new TableGroupUnGroupEvent(orderTables));
        orderTables.forEach(OrderTable::unGroup);
        tableGroupRepository.save(tableGroup);
    }
}
