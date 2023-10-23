package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private static final List<String> INCLUDE_ORDER_STATUS_NAME
            = List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

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
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<OrderTable> orderTables = request.getOrderTables();
        validateOrderTableSize(orderTables);

        final List<OrderTable> savedOrderTables = findAllOrderTablesByIdIn(orderTables);
        validateMatchOrderTableSize(orderTables, savedOrderTables);
        validateEmptyTable(savedOrderTables);

        final TableGroup savedTableGroup = saveTableGroup(request);

        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroupId(tableGroupId);
            savedOrderTable.setEmpty(false);
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables);

        return TableGroupResponse.toResponse(savedTableGroup);
    }

    private void validateEmptyTable(final List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException("[ERROR] 빈 테이블이 생성되지 않았습니다.");
            }
        }
    }

    private void validateMatchOrderTableSize(final List<OrderTable> orderTables,
                                             final List<OrderTable> savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("[ERROR] 저장된 데이터의 수와 실제 주문 테이블의 수가 다릅니다.");
        }
    }

    private TableGroup saveTableGroup(final TableGroupCreateRequest request) {
        return tableGroupDao.save(
                new TableGroup(
                        LocalDateTime.now(),
                        request.getOrderTables()
                )
        );
    }

    private List<OrderTable> findAllOrderTablesByIdIn(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        return orderTableDao.findAllByIdIn(orderTableIds);
    }

    private void validateOrderTableSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("[ERROR] 주문하는 테이블이 없거나, 단체 주문 대상이 아닙니다.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        validateAllOrderComplement(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTable.setEmpty(false);
            orderTableDao.save(orderTable);
        }
    }

    private void validateAllOrderComplement(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, INCLUDE_ORDER_STATUS_NAME)) {
            throw new IllegalArgumentException("[ERROR] 아직 모든 주문이 완료되지 않았습니다.");
        }
    }
}
