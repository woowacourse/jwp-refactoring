package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableGroupCreateRequest.OrderTableGroupRequest;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.TableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupRepository tableGroupRepository;
    private final TableValidator tableValidator;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableGroupRepository tableGroupRepository,
                             final TableValidator tableValidator) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupRepository = tableGroupRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableGroup create(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = mapToIds(request);
        tableValidator.validateTableGroup(orderTableIds);
        TableGroup tableGroup = new TableGroup(orderTableDao.findAllByIdIn(orderTableIds));
        tableGroup.validateOrderTableSize(orderTableIds.size());

        return tableGroupRepository.save(tableGroup);
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
