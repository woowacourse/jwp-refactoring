package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderTableIdDto;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
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

    public TableGroupService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByOrderTableIdsIn(
                extractOrderTableIds(request.getOrderTableIdsDto()));
        validateEmptyOrderTable(orderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(request.toTableGroup(LocalDateTime.now()));
        fillOrderTableGroup(orderTables, savedTableGroup);
        orderTableRepository.saveAll(orderTables);
        return TableGroupResponse.from(savedTableGroup, orderTables);
    }

    private void fillOrderTableGroup(final List<OrderTable> orderTables, final TableGroup savedTableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.fillOrderTableGroup(savedTableGroup.getId());
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        validateCompletion(orderTableIds);
        ungroupAll(orderTables);
    }

    private void ungroupAll(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.clear();
            orderTableRepository.save(orderTable);
        }
    }

    private void validateOrderTableSize(final List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private List<Long> extractOrderTableIds(final List<OrderTableIdDto> orderTableIdsDto) {
        final List<Long> orderTableIds = orderTableIdsDto.stream()
                .map(OrderTableIdDto::getOrderTableId)
                .collect(Collectors.toList());

        validateOrderTableSize(orderTableIds);
        return orderTableIds;
    }

    private void validateEmptyOrderTable(final List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void validateCompletion(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
