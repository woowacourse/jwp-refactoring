package kitchenpos.application.tableGroup;

import kitchenpos.common.GroupOrderTablesEvent;
import kitchenpos.common.UngroupOrderTableEvent;
import kitchenpos.domain.tableGroup.TableGroup;
import kitchenpos.domain.tableGroup.TableGroupRepository;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.exception.tableGroupException.TableGroupNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final ApplicationEventPublisher eventPublisher) {
        this.tableGroupRepository = tableGroupRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTables().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
        final TableGroup tableGroup = new TableGroup();
        tableGroup.validate(orderTableIds);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        eventPublisher.publishEvent(new GroupOrderTablesEvent(tableGroupRequest.getOrderTables(), savedTableGroup.getId()));

        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(TableGroupNotFoundException::new);

        eventPublisher.publishEvent(new UngroupOrderTableEvent(tableGroup.getId()));
    }
}
