package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.service.TableValidator;
import kitchenpos.ui.dto.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;
    private final TableValidator tableValidator;

    public TableGroupService(
            final OrderDao orderDao,
            final OrderTableDao orderTableDao,
            final TableGroupDao tableGroupDao,
            final TableValidator tableValidator
    ) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableGroup create(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = request.getOrderTableIds();
        int orderTableCount = request.size();

        if (orderTableCount < 2) {
            throw new IllegalArgumentException();
        }

        final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);
        if (orderTableCount != orderTables.size()) {
            throw new IllegalArgumentException();
        }

        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(null, LocalDateTime.now(), new ArrayList<>()));
        for (final OrderTable orderTable : orderTables) {
            tableGroup.add(orderTableDao.save(orderTable.groupBy(tableGroup.getId())));
        }
        return tableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        for (final OrderTable orderTable : orderTables) {
            tableValidator.validateOrderStatusOfTable(orderTable.getId());
            orderTableDao.save(orderTable.ungroup());
        }
    }
}
