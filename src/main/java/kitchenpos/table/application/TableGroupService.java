package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableGroupRequest;
import kitchenpos.table.event.UngroupEvent;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository,
        ApplicationEventPublisher applicationEventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public TableGroup create(final OrderTableGroupRequest orderTableGroupRequest) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(
            orderTableGroupRequest.getTableGroupIds());

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = getTableGroup(tableGroupId);

        applicationEventPublisher.publishEvent(new UngroupEvent(tableGroup.getOrderTables()));

        tableGroup.ungroup();
    }

    private TableGroup getTableGroup(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
    }
}
