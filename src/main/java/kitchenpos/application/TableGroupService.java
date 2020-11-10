package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.dto.table.OrderTableDto;
import kitchenpos.dto.table.TableGroupRequest;
import kitchenpos.dto.table.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTableDto> orderTableDtos = tableGroupRequest.getOrderTables();
        List<OrderTable> savedOrderTables = orderTableDtos.stream()
                .map(orderTableDto -> orderTableRepository.findById(orderTableDto.getId()).orElseThrow(IllegalArgumentException::new))
                .collect(Collectors.toList());

        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.of(savedOrderTables));

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.tableGrouping(savedTableGroup);
        }

        return TableGroupResponse.of(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        for (final OrderTable orderTable : orderTables) {
            List<Order> ordersByOrderTable = orderRepository.findAllByOrderTable(orderTable);
            orderTable.tableUngrouping(ordersByOrderTable);
            orderTableRepository.save(orderTable);
        }
    }
}
