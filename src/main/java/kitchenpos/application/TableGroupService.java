package kitchenpos.application;

import kitchenpos.application.request.tablegroup.OrderTableIdRequest;
import kitchenpos.application.request.tablegroup.TableGroupRequest;
import kitchenpos.application.response.ResponseAssembler;
import kitchenpos.application.response.tablegroup.TableGroupResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;
    private final ResponseAssembler responseAssembler;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao,
                             final TableGroupDao tableGroupDao, final ResponseAssembler responseAssembler) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.responseAssembler = responseAssembler;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<Long> orderTableIds = request.getOrderTables()
                .stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toUnmodifiableList());
        validateOrderTablesMoreThanSingle(orderTableIds);

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        validateAllOrderTablesNotDuplicated(orderTableIds, savedOrderTables);
        validateAllOrderTablesEmpty(savedOrderTables);

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        final Long tableGroupId = savedTableGroup.getId();
        for (final var savedOrderTable : savedOrderTables) {
            savedOrderTable.belongToTableGroup(tableGroupId);
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables);

        return responseAssembler.tableGroupResponse(savedTableGroup);
    }

    private void validateOrderTablesMoreThanSingle(final List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException("2개 이상의 주문 테이블을 지정해야 합니다.");
        }
    }

    private void validateAllOrderTablesNotDuplicated(final List<Long> orderTableIds, final List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("중복되는 주문 테이블이 있습니다.");
        }
    }

    private void validateAllOrderTablesEmpty(final List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException("비어있지 않은 주문 테이블이 존재합니다.");
            }
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        validateAllOrderTablesCompleted(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.unbelongToTableGroup();
            orderTableDao.save(orderTable);
        }
    }

    private void validateAllOrderTablesCompleted(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("계산이 완료되지 않은 테이블이 존재합니다.");
        }
    }
}
