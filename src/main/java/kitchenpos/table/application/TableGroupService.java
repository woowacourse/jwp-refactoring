package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableValidator;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.dto.request.TableGroupRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;
    private final TableValidator tableValidator;

    public TableGroupService(final OrderTableDao orderTableDao,
                             final TableGroupDao tableGroupDao,
                             final TableValidator tableValidator) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTableRequest> orderTableRequests = tableGroupRequest.getOrderTables();
        validateOrderTableRequestSize(orderTableRequests);

        final List<Long> orderTableIds = tableGroupRequest.getOrderTablesIds();
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        validateExistOrderTable(orderTableIds, savedOrderTables);
        savedOrderTables.forEach(this::validateNonEmptyOrderTableAndNullTableGroup);

        final TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
        final List<OrderTable> orderTables = saveOrderTables(savedOrderTables, savedTableGroup);

        return TableGroupResponse.of(savedTableGroup, orderTables);
    }

    private void validateOrderTableRequestSize(List<OrderTableRequest> orderTableRequests) {
        if (CollectionUtils.isEmpty(orderTableRequests) || orderTableRequests.size() < 2) {
            throw new IllegalArgumentException("주문 테이블의 수는 최소 2개 이상이어야합니다.");
        }
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
        tableValidator.validateTableGroup(orderTables);
        orderTables.stream()
                .peek(OrderTable::ungroup)
                .forEach(orderTableDao::save);
    }


}
