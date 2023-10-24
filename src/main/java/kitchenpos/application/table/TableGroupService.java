package kitchenpos.application.table;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.table.dto.CreateTableGroupCommand;
import kitchenpos.application.table.dto.CreateTableGroupResponse;
import kitchenpos.application.table.dto.UngroupTableGroupCommand;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.domain.table.TableUngroupedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final ApplicationEventPublisher publisher;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            ApplicationEventPublisher publisher,
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository
    ) {
        this.publisher = publisher;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public CreateTableGroupResponse create(CreateTableGroupCommand command) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdInOrElseThrow(command.orderTableIds());
        TableGroup tableGroup = new TableGroup(orderTables);
        return CreateTableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    @Transactional
    public void ungroup(UngroupTableGroupCommand command) {
        TableGroup tableGroup = tableGroupRepository.getById(command.tableGroupId());
        List<Long> orderTableIds = tableGroup.orderTables().stream()
                .map(OrderTable::id)
                .collect(Collectors.toList());
        publisher.publishEvent(new TableUngroupedEvent(orderTableIds));
        tableGroup.ungroup();
    }
}
