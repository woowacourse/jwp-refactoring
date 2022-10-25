package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(OrderDao orderDao, OrderTableDao orderTableDao, TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();

        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹은 2개 이상부터 지정할 수 있습니다.");
        }

        List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블로 지정할 수 업습니다.");
        }

        for (OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException("비어 있는 테이블로만 단체를 지정할 수 있습니다.");
            }
        }

        TableGroup savedTableGroup = tableGroupDao.save(tableGroupRequest.toEntity(LocalDateTime.now()));

        Long tableGroupId = savedTableGroup.getId();
        for (OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.updateTableGroupId(tableGroupId);
            savedOrderTable.changeEmpty(false);
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.addOrderTables(savedOrderTables);

        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(kitchenpos.domain.OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("완료되지 않은 주문이 있어서 해제할 수 없습니다.");
        }

        for (OrderTable orderTable : orderTables) {
            orderTable.updateTableGroupId(null);
            orderTable.changeEmpty(false);
            orderTableDao.save(orderTable);
        }
    }
}
