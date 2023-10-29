package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableInTableGroupDto;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.event.TableGroupCreateEvent;
import kitchenpos.event.TableGroupDeleteEvent;
import kitchenpos.event.ValidateSameSizeOrderTableEvent;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher publisher;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final ApplicationEventPublisher publisher
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.publisher = publisher;
    }

    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = request.getOrderTables().stream()
                .map(OrderTableInTableGroupDto::getId)
                .collect(Collectors.toList());

        publisher.publishEvent(new ValidateSameSizeOrderTableEvent(orderTableIds));

        final TableGroup tableGroup = TableGroup.create();
        tableGroupRepository.save(tableGroup);

        publisher.publishEvent(new TableGroupCreateEvent(tableGroup.getId(), orderTableIds));

        return TableGroupResponse.from(tableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        publisher.publishEvent(new TableGroupDeleteEvent(tableGroupId));
    }
}
