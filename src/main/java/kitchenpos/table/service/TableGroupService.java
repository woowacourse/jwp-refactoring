package kitchenpos.table.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.domain.type.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.request.CreateTableGroupRequest;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
import kitchenpos.exception.EmptyTableException;
import kitchenpos.exception.NoSuchDataException;
import kitchenpos.exception.InvalidOrderStateException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final CreateTableGroupRequest request) {
        final List<OrderTableRequest> orderTables = request.getOrderTables();

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new InvalidOrderStateException("테이블 2개 이상부터 그룹을 형성할 수 있습니다.");
        }

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new NoSuchDataException("입력한 테이블들이 존재하지 않습니다.");
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new EmptyTableException("비어있지 않거나 테이블 그룹이 형성된 테이블은 테이블을 형성할 수 없습니다.");
            }
        }

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        savedOrderTables.stream()
                .forEach(orderTable -> orderTable.group(savedTableGroup.getId()));

        return TableGroupResponse.from(savedTableGroup,savedOrderTables);
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidOrderStateException("조리 중이거나 식사 중인 테이블 그룹을 해제할 수 없습니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            final OrderTable ungroupTable = OrderTable.builder()
                    .id(orderTable.getId())
                    .numberOfGuests(orderTable.getNumberOfGuests())
                    .empty(false)
                    .build();

            orderTableRepository.save(ungroupTable);
        }
    }
}
