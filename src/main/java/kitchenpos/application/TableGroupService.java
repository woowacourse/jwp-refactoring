package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.IdRequest;
import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.application.dto.TableGroupResponse;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupVerifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;
    private final TableGroupVerifier tableGroupVerifier;

    public TableGroupService(
        final OrderTableDao orderTableDao,
        final TableGroupDao tableGroupDao,
        final TableGroupVerifier tableGroupVerifier
    ) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.tableGroupVerifier = tableGroupVerifier;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest tableGroupCreateRequest) {
        final List<Long> orderTableIds = tableGroupCreateRequest.getOrderTables()
            .stream()
            .map(IdRequest::getId)
            .collect(Collectors.toList());

        tableGroupVerifier.verifyOrderTableSize(orderTableIds);

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블은 단체 지정할 수 없습니다.");
        }

        TableGroup savedTableGroup = tableGroupDao.save(tableGroupCreateRequest.toEntity());

        for (final OrderTable orderTable : savedOrderTables) {
            orderTable.groupBy(savedTableGroup.getId());
            orderTableDao.save(orderTable);
        }

        return TableGroupResponse.of(savedTableGroup, savedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        tableGroupVerifier.verifyNotCompletedOrderStatus(orderTableIds);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
            orderTableDao.save(orderTable);
        }
    }
}
