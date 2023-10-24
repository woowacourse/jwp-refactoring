package kitchenpos.tablegroup.application;


import java.util.List;
import kitchenpos.common.event.TablesGroupedEvent;
import kitchenpos.common.event.TablesUngroupedEvent;
import kitchenpos.tablegroup.application.dto.TableGroupRequest;
import kitchenpos.tablegroup.application.dto.TableGroupResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher publisher;

    public TableGroupService(
            TableGroupRepository tableGroupRepository,
            ApplicationEventPublisher publisher
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.publisher = publisher;
    }

    public TableGroupResponse group(final TableGroupRequest tableGroupRequest) {
        TableGroup tableGroup = tableGroupRepository.save(TableGroup.create());

        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        publisher.publishEvent(new TablesGroupedEvent(tableGroup.getId(), orderTableIds));

        return TableGroupResponse.from(tableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        publisher.publishEvent(new TablesUngroupedEvent(tableGroupId));

        tableGroupRepository.delete(tableGroup);
    }


}
