package kitchenpos.application.table;

import static kitchenpos.exception.table.TableGroupExceptionType.ORDER_TABLES_CAN_NOT_LESS_THAN_TWO;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.table.dto.CreateTableGroupCommand;
import kitchenpos.application.table.dto.CreateTableGroupResponse;
import kitchenpos.application.table.dto.UngroupTableGroupCommand;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.event.TableUngroupedEvent;
import kitchenpos.exception.table.TableGroupException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
        validateOrderTables(orderTables);
        TableGroup tableGroup = new TableGroup();
        orderTables.forEach(it -> it.group(tableGroup));
        return CreateTableGroupResponse.from(tableGroupRepository.save(tableGroup), orderTables);
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new TableGroupException(ORDER_TABLES_CAN_NOT_LESS_THAN_TWO);
        }
    }

    @Transactional
    public void ungroup(UngroupTableGroupCommand command) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(command.tableGroupId());
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::id)
                .collect(Collectors.toList());
        publisher.publishEvent(new TableUngroupedEvent(orderTableIds));
        orderTables.forEach(OrderTable::ungroup);
    }
}
