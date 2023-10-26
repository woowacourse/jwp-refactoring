package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.TableGroup;
import kitchenpos.tablegroup.application.dto.request.TableGroupCreateRequest;
import kitchenpos.tablegroup.application.dto.response.TableGroupResponse;
import kitchenpos.tablegroup.application.event.TableGroupCreateRequestEvent;
import kitchenpos.tablegroup.application.event.TableGroupDeleteRequestEvent;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Transactional(readOnly = true)
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

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest orderTableIds) {
        validateTableGroupInput(orderTableIds.getOrderTableIds());
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        eventPublisher.publishEvent(new TableGroupCreateRequestEvent(orderTableIds.getOrderTableIds(), tableGroup));
        return TableGroupResponse.of(tableGroup, orderTableIds.getOrderTableIds());
    }

    private void validateTableGroupInput(final List<Long> idsInput) {
        if (CollectionUtils.isEmpty(idsInput) || idsInput.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findMandatoryById(tableGroupId);
        final TableGroupDeleteRequestEvent event = new TableGroupDeleteRequestEvent(tableGroup);
        eventPublisher.publishEvent(event);
    }
}
