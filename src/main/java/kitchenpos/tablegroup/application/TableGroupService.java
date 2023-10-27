package kitchenpos.tablegroup.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.ValidateOrderIsNotCompletionDto;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.dto.OrderTableInTableGroupDto;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher publisher;

    public TableGroupService(
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository,
            final ApplicationEventPublisher publisher
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.publisher = publisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<OrderTable> orderTables = findAllOrderTablesByIdIn(request);

        final TableGroup tableGroup = TableGroup.create();
        tableGroupRepository.save(tableGroup);
        tableGroup.appendOrderTables(orderTables);

        return TableGroupResponse.from(tableGroup);
    }

    private List<OrderTable> findAllOrderTablesByIdIn(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = request.getOrderTables().stream()
                .map(OrderTableInTableGroupDto::getId)
                .collect(Collectors.toList());

        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("[ERROR] 저장된 데이터의 수와 실제 주문 테이블의 수가 다릅니다.");
        }

        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        validateAllOrderComplement(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    private void validateAllOrderComplement(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        publisher.publishEvent(new ValidateOrderIsNotCompletionDto(orderTableIds));
    }
}
