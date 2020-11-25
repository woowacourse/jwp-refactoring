package kitchenpos.application;

import kitchenpos.domain.order.*;
import kitchenpos.dto.order.OrderTableRequest;
import kitchenpos.dto.order.TableGroupCreateRequest;
import kitchenpos.dto.order.TableGroupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TableGroupService {
    private static final String INVALID_GROUP_REQUEST_EXCEPTION_MESSAGE = "그룹화하려는 테이블의 상태가 유효하지 않습니다.";
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        List<OrderTable> orderTables = findOrderTables(request);
        TableGroup tableGroup = new TableGroup(orderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        for (final OrderTable orderTable : orderTables) {
            OrderTable updatedOrderTable = new OrderTable(
                    orderTable.getId(),
                    orderTable.getNumberOfGuests(),
                    orderTable.getEmpty(),
                    tableGroup
            );
            orderTableRepository.save(updatedOrderTable);
        }

        return new TableGroupResponse(savedTableGroup);
    }

    private List<OrderTable> findOrderTables(final TableGroupCreateRequest request) {
        final List<OrderTableRequest> orderTables = request.getOrderTables();

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException(INVALID_GROUP_REQUEST_EXCEPTION_MESSAGE);
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.getEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException(INVALID_GROUP_REQUEST_EXCEPTION_MESSAGE);
            }
        }

        return savedOrderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("해당 테이블 그룹을 찾을 수 없습니다."));

        ungroupTables(tableGroup);

        tableGroupRepository.delete(tableGroup);
    }

    private void ungroupTables(final TableGroup tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                    savedOrderTable.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
                throw new IllegalArgumentException("주문이 완료되지 않아, 테이블을 그룹 해제할 수 없습니다.");
            }
            OrderTable updatedOrderTable = new OrderTable(
                    savedOrderTable.getId(),
                    savedOrderTable.getNumberOfGuests(),
                    savedOrderTable.getEmpty(),
                    null
            );
            orderTableRepository.save(updatedOrderTable);
        }
    }
}
