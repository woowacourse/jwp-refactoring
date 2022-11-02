package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.request.CreateTableGroupRequest;
import kitchenpos.order.dto.response.TableGroupResponse;
import kitchenpos.order.event.UngroupEvent;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.order.repository.TableGroupRepository;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher publisher;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository,
        ApplicationEventPublisher publisher) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.publisher = publisher;
    }

    @Transactional
    public TableGroupResponse create(final CreateTableGroupRequest request) {
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(getOrderTables(request)));

        return new TableGroupResponse(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        validateOrderTableById(tableGroupId);
        publisher.publishEvent(new UngroupEvent(tableGroupId));
    }

    private List<OrderTable> getOrderTables(final CreateTableGroupRequest request) {
        final List<Long> orderTableIds = request.getOrderTables().stream()
            .map(it -> it.getId())
            .collect(Collectors.toList());

        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("존재하지 않은 테이블입니다.");
        }

        return orderTables;
    }

    private void validateOrderTableById(Long orderTableId) {
        orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 테이블입니다."));
    }

}
