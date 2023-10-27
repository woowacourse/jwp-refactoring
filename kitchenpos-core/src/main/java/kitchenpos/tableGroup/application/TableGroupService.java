package kitchenpos.tableGroup.application;

import kitchenpos.common.GroupOrderTablesEvent;
import kitchenpos.common.UngroupOrderTableEvent;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import kitchenpos.tableGroup.exception.TableGroupNotFoundException;
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

        eventPublisher.publishEvent(new GroupOrderTablesEvent(orderTableIds, savedTableGroup.getId()));

        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(TableGroupNotFoundException::new);

        eventPublisher.publishEvent(new UngroupOrderTableEvent(tableGroup.getId()));
    }
}
