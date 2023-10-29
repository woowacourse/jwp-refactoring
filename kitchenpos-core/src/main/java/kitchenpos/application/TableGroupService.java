package kitchenpos.application;

import kitchenpos.application.event.AddGroupTableEvent;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.dto.request.CreateTableGroupRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final ApplicationEventPublisher orderTableEventPublisher;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final ApplicationEventPublisher orderTableEventPublisher,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository
    ) {
        this.orderTableEventPublisher = orderTableEventPublisher;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final CreateTableGroupRequest orderTableRequest) {
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        orderTableEventPublisher.publishEvent(new AddGroupTableEvent(tableGroup, orderTableRequest.getOrderTables()));
        return tableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        validateOrderTableProgress(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroupById(tableGroupId);
            orderTableRepository.save(orderTable);
        }
    }

    private void validateOrderTableProgress(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                                                    .map(OrderTable::getId)
                                                    .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))
        ) {
            throw new IllegalArgumentException();
        }
    }
}
