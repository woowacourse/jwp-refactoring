package kitchenpos.application.tablegroup;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.domain.tablegroup.TableGroupUngroupedEvent;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableGroupService(
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public TableGroupResponse create(TableGroupRequest request) {
        TableGroup tableGroup = TableGroup.create();
        tableGroup.group(orderTableRepository.findAllByIdIn(request.getOrderTableIds()));
        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    public void ungroup(Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        orderTables.forEach(OrderTable::ungroup);
        applicationEventPublisher.publishEvent(new TableGroupUngroupedEvent(tableGroupId));
    }
}
