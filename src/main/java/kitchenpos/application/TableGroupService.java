package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao,
                             final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> orderTables = tableGroupRequest.getOrderTables().stream()
                .map(orderTableRequest -> orderTableDao.findById(orderTableRequest.getId())
                        .orElseThrow(() -> new IllegalArgumentException("등록되는 모든 테이블들은 존재해야 한다.")))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("등록되는 테이블 수가 2 이상이어야 한다.");
        }

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("등록되는 모든 테이블들은 존재해야 한다.");
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException("등록되는 모든 테이블들은 기존 단체 지정이 없어야 한다.");
            }
            if (!savedOrderTable.isEmpty()) {
                throw new IllegalArgumentException("등록되는 모든 테이블들은 비어있어야 한다.");
            }
        }

        TableGroup timeSetTable = new TableGroup(null, LocalDateTime.now(), orderTables);

        final TableGroup savedTableGroup = tableGroupDao.save(timeSetTable);

        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.updateTableGroupId(tableGroupId);
            savedOrderTable.updateEmpty(false);
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.updateOrderTables(savedOrderTables);

        return toTableGroupResponse(savedOrderTables, savedTableGroup);
    }

    private TableGroupResponse toTableGroupResponse(List<OrderTable> savedOrderTables, TableGroup savedTableGroup) {
        return new TableGroupResponse(savedTableGroup.getId(), savedTableGroup.getCreatedDate(),
                getOrderTableResponses(savedOrderTables));
    }

    private List<OrderTableResponse> getOrderTableResponses(List<OrderTable> savedOrderTables) {
        return savedOrderTables.stream()
                .map(this::toOrderTableResponse)
                .collect(Collectors.toList());
    }

    private OrderTableResponse toOrderTableResponse(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("단체 지정 속 모든 테이블들의 주문이 있다면 COMPLETION 상태여야 한다.");
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.updateTableGroupId(null);
            orderTable.updateEmpty(false);
            orderTableDao.save(orderTable);
        }
    }
}
