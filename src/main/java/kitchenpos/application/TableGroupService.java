package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.IdRequest;
import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.application.dto.TableGroupResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupVerifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;
    private final TableGroupVerifier tableGroupVerifier;

    public TableGroupService(
        final OrderDao orderDao,
        final OrderTableDao orderTableDao,
        final TableGroupDao tableGroupDao,
        final TableGroupVerifier tableGroupVerifier
    ) {
        this.orderDao = orderDao;
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

        final TableGroup tableGroup = tableGroupDao
            .save(tableGroupVerifier.toTableGroup(orderTableIds));

        final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);

        for (final OrderTable orderTable : orderTables) {
            orderTable.groupBy(tableGroup.getId());
            orderTableDao.save(orderTable);
        }

        return TableGroupResponse.of(tableGroup, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        tableGroupVerifier.verifyNotCompleted(orderTableIds);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
            orderTableDao.save(orderTable);
        }
    }
}
