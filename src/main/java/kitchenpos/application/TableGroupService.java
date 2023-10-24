package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.tablegroup.TableGroupRequest;
import kitchenpos.application.dto.tablegroup.TableGroupResponse;
import kitchenpos.application.dto.tablegroup.TableOfGroupDto;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        final List<OrderTable> orderTables = convertToOrderTables(tableGroupRequest.getOrderTables());

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        tableGroup.addOrderTables(orderTables);
        tableGroupRepository.save(tableGroup);

        for (final OrderTable orderTable : orderTables) {
            orderTable.groupBy(tableGroup);
        }

        return TableGroupResponse.from(tableGroup);
    }

    private List<OrderTable> convertToOrderTables(final List<TableOfGroupDto> tables) {
        return tables.stream()
            .map(tableRequest -> getOrderTable(tableRequest.getId()))
            .collect(Collectors.toList());
    }

    private OrderTable getOrderTable(final Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(IllegalArgumentException::new);

        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        if (orderRepository.existsByOrderTableInAndOrderStatusIn(orderTables, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }
}
