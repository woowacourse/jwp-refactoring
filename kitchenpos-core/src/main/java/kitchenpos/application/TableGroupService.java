package kitchenpos.application;

import kitchenpos.application.event.AddGroupTableEvent;
import kitchenpos.application.event.UngroupTableEvent;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.dto.request.CreateTableGroupRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TableGroupService {

    private final ApplicationEventPublisher orderTableEventPublisher;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final ApplicationEventPublisher orderTableEventPublisher,
            final TableGroupRepository tableGroupRepository
    ) {
        this.orderTableEventPublisher = orderTableEventPublisher;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final CreateTableGroupRequest orderTableRequest) {
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        orderTableEventPublisher.publishEvent(new AddGroupTableEvent(tableGroup, orderTableRequest.getOrderTables()));
        return tableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        orderTableEventPublisher.publishEvent(new UngroupTableEvent(tableGroupId));
    }
}
