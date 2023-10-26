package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupGroupedEvent;
import kitchenpos.domain.TableGroupUngroupedEvent;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final ApplicationEventPublisher applicationEventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(request.getOrderTables());
        if (orderTables.size() != request.getOrderTables().size()) {
            throw new IllegalArgumentException();
        }

        final TableGroup tableGroup = new TableGroup(orderTables);
        applicationEventPublisher.publishEvent(new TableGroupGroupedEvent(tableGroup));
        return TableGroupResponse.toResponse(tableGroupRepository.save(tableGroup), orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
        applicationEventPublisher.publishEvent(new TableGroupUngroupedEvent(tableGroup));
        tableGroupRepository.deleteById(tableGroupId);
    }
}
