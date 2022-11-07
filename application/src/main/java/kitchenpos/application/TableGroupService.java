package kitchenpos.application;

import kitchenpos.application.dto.request.CreateTableGroupDto;
import kitchenpos.application.dto.response.TableGroupDto;
import kitchenpos.common.domain.order.OrderStatus;
import kitchenpos.common.repository.order.OrderRepository;
import kitchenpos.common.repository.table.OrderTableRepository;
import kitchenpos.common.repository.table.TableGroupRepository;
import kitchenpos.common.domain.table.OrderTable;
import kitchenpos.common.domain.table.OrderTables;
import kitchenpos.common.domain.table.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository,
                             OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupDto create(CreateTableGroupDto createTableGroupDto) {
        final List<Long> tableIds = createTableGroupDto.getOrderTableIds();
        final OrderTables orderTables = findValidOrderTables(tableIds);
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        orderTables.group(tableGroup.getId());
        List<OrderTable> groupedTables = orderTables.getValue();
        for (final OrderTable savedOrderTable : groupedTables) {
            orderTableRepository.save(savedOrderTable);
        }
        return TableGroupDto.of(tableGroup, groupedTables);
    }

    private OrderTables findValidOrderTables(List<Long> tableIds) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(tableIds);
        if (tableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
        return new OrderTables(orderTables);
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        validateNoOngoingOrder(orderTables);
        for (final OrderTable orderTable : orderTables) {
            orderTable.removeTableGroupId();
            orderTableRepository.save(orderTable);
        }
    }

    private void validateNoOngoingOrder(List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, OrderStatus.getOngoingStatuses())) {
            throw new IllegalArgumentException("식사 중인 테이블은 그룹화를 제거할 수 없습니다.");
        }
    }
}
