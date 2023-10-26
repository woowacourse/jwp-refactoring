package kitchenpos.application.tablegroup;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.dto.TableGroupRequest;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher publisher;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final ApplicationEventPublisher publisher) {
        this.tableGroupRepository = tableGroupRepository;
        this.publisher = publisher;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest request) {
        final TableGroup tableGroup = TableGroup.create();
        tableGroup.validateGrouping(request.getOrderTableIds());
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        publisher.publishEvent(new GroupingEvent(request.getOrderTableIds(), savedTableGroup.getId()));

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("없는 그룹이에요."));

        tableGroupRepository.delete(tableGroup);
        publisher.publishEvent(new UngroupingEvent(tableGroup.getId()));
    }
}
