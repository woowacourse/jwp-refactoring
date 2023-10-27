package kitchenpos.tablegroup.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.dto.OrderTableInTableGroupDto;
import kitchenpos.order.dto.UpdateGroupOrderTableDto;
import kitchenpos.order.dto.UpdateUngroupOrderTableDto;
import kitchenpos.order.dto.ValidateAppendOrderTableInTableGroupDto;
import kitchenpos.order.dto.ValidateOrderIsNotCompletionInOrderTableDto;
import kitchenpos.order.dto.ValidateSameSizeOrderTableDto;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
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

        publisher.publishEvent(new ValidateSameSizeOrderTableDto(orderTableIds));

        final TableGroup tableGroup = TableGroup.create();
        tableGroupRepository.save(tableGroup);

        publisher.publishEvent(new ValidateAppendOrderTableInTableGroupDto(orderTableIds));
        publisher.publishEvent(new UpdateGroupOrderTableDto(tableGroup.getId(), orderTableIds));

        return TableGroupResponse.from(tableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        publisher.publishEvent(new ValidateOrderIsNotCompletionInOrderTableDto(tableGroupId));
        publisher.publishEvent(new UpdateUngroupOrderTableDto(tableGroupId));
    }
}
