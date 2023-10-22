package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.table.TableGroupCreateRequest;
import kitchenpos.dto.table.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(request.getOrderTables());
        TableGroup tableGroup = request.toTableGroup(savedOrderTables);

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        groupAll(savedOrderTables, savedTableGroup.getId());

        savedTableGroup.setOrderTables(savedOrderTables);
        return TableGroupResponse.from(savedTableGroup);
    }

    private void groupAll(List<OrderTable> orderTables, Long tableGroupId) {
        orderTables.forEach(orderTable -> {
            orderTable.group(tableGroupId);
            orderTableDao.save(orderTable);
        });
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 단체 지정입니다."));

        boolean hasCookingOrMealOrder = orderDao.existsByOrderTableIdInAndOrderStatusIn(
                tableGroup.getOrderTableIds(),
                List.of(COOKING.name(), MEAL.name())
        );

        tableGroup.ungroup(hasCookingOrMealOrder);
        tableGroup.getOrderTables().forEach(orderTableDao::save);
    }
}
