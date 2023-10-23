package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
import org.springframework.util.CollectionUtils;

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
        if (CollectionUtils.isEmpty(tableGroupRequest.getOrderTables()) || tableGroupRequest.getOrderTables().size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<OrderTable> savedOrderTables = convertToOrderTables(tableGroupRequest.getOrderTables());

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.groupBy(savedTableGroup);
        }
        savedTableGroup.addOrderTables(savedOrderTables);

        return TableGroupResponse.from(savedTableGroup);
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
