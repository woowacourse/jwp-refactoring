package kitchenpos.application.tablegroup;

import kitchenpos.domain.tablegroup.*;
import kitchenpos.dto.request.CreateTableGroupRequest;
import kitchenpos.dto.response.CreateTableGroupResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static kitchenpos.dto.request.CreateTableGroupRequest.CreateOrderTable;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;
    private final ApplicationEventPublisher publisher;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final TableGroupValidator tableGroupValidator,
            final ApplicationEventPublisher publisher) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
        this.publisher = publisher;
    }

    @Transactional
    public CreateTableGroupResponse create(final CreateTableGroupRequest request) {
        tableGroupValidator.validateCreate(request);

        final TableGroup tableGroup = tableGroupRepository.save(TableGroup.builder()
                .createdDate(LocalDateTime.now())
                .build());
        
        publisher.publishEvent(new TableGroupCreatedEvent(tableGroup.getId(), request.getOrderTables().stream()
                .map(CreateOrderTable::getId)
                .collect(Collectors.toList())));

        return CreateTableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
        tableGroupValidator.validateUngroup(tableGroup);
        publisher.publishEvent(new TableUngroupedEvent(tableGroup.getId()));
    }
}
