package kitchenpos.tablegroup.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.tablegroup.application.dto.GroupOrderTableRequest;
import kitchenpos.tablegroup.application.dto.TableGroupResult;
import kitchenpos.tablegroup.application.dto.TableGroupingRequest;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroupingEvent;
import kitchenpos.tablegroup.domain.TableUngroupEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final ApplicationEventPublisher eventPublisher
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.eventPublisher = eventPublisher;
    }

    public TableGroupResult create(final TableGroupingRequest request) {
        final List<Long> orderTableIds = extractTableIdsFrom(request);
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        eventPublisher.publishEvent(new TableGroupingEvent(tableGroup.getId(), orderTableIds));
        return TableGroupResult.from(tableGroup);
    }

    private List<Long> extractTableIdsFrom(final TableGroupingRequest request) {
        return request.getOrderTables().stream()
                .map(GroupOrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    public void ungroup(final Long ungroupTableId) {
        final TableGroup tableGroup = tableGroupRepository.findById(ungroupTableId)
                .orElseThrow(() -> new IllegalArgumentException("Table group does not exist."));
        eventPublisher.publishEvent(new TableUngroupEvent(tableGroup.getId()));
    }
}
