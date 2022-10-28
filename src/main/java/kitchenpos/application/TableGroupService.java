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
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao,
                             final OrderTableDao orderTableDao,
                             final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTableRequest> orderTableRequests = tableGroupRequest.getOrderTables();
        final List<Long> orderTableIds = tableGroupRequest.getOrderTablesIds();
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        validateExistOrderTable(orderTableIds, savedOrderTables);
        savedOrderTables.forEach(this::validateNonEmptyOrderTableAndNullTableGroup);

        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
        final List<OrderTable> orderTables = saveOrderTables(savedOrderTables, savedTableGroup);

        return TableGroupResponse.of(savedTableGroup, orderTables);
    }

    private void validateNonEmptyOrderTableAndNullTableGroup(OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException("주문 테이블은 비어있을 수 없고 테이블 그룹은 비어있어야합니다.");
        }
    }

    private void validateExistOrderTable(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("요청 주문 테이블과 조회된 주문 테이블의 수는 같아야합니다.");
        }
    }

    private List<OrderTable> saveOrderTables(List<OrderTable> savedOrderTables, TableGroup savedTableGroup) {
        return savedOrderTables.stream()
                .peek(orderTable -> orderTable.group(savedTableGroup.getId()))
                .map(orderTableDao::save)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        validateOrderStatus(orderTables);

        orderTables.stream()
                .peek(OrderTable::ungroup)
                .forEach(orderTableDao::save);
    }

    private void validateOrderStatus(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문이 Cooking이거나 Meal인 경우 그룹 해제할 수 없습니다.");
        }
    }
}
