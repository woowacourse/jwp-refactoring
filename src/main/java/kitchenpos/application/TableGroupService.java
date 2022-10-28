package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.request.TableGroupCreateRequest;
import kitchenpos.application.request.TableGroupCreateRequest.OrderTableGroupRequest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroup create(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = mapToIds(request);
        TableGroup tableGroup = new TableGroup(orderTableDao.findAllByIdIn(orderTableIds));
        tableGroup.validateOrderTableSize(orderTableIds.size());

        return tableGroupDao.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        validateCookingOrMeal(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTableDao.save(orderTable.emptyTable());
        }
    }

    private static List<Long> mapToIds(final TableGroupCreateRequest request) {
        return request.getOrderTables()
                .stream()
                .map(OrderTableGroupRequest::getId)
                .collect(Collectors.toList());
    }

    private void validateCookingOrMeal(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리 혹은 식사중인 테이블이 있어 단체를 해제할 수 없습니다.");
        }
    }
}
