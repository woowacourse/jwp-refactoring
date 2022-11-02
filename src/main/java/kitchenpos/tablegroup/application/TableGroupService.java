package kitchenpos.tablegroup.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.ordertable.dao.OrderTableDao;
import kitchenpos.ordertable.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.tablegroup.dao.TableGroupDao;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.request.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
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
        final OrderTables requestOrderTables = new OrderTables(request.getOrderTables());
        final OrderTables savedOrderTables = new OrderTables(
            orderTableDao.findAllByIdIn(requestOrderTables.getIds()));

        savedOrderTables.validateSameSize(requestOrderTables);
        savedOrderTables.validateNotGroupAll();

        final TableGroup tableGroup = tableGroupDao.save(request.toEntity());
        final List<OrderTable> orderTables = saveOrderTables(
            savedOrderTables.getOrderTables(), tableGroup.getId());

        return TableGroupResponse.of(tableGroup, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(
            orderTableDao.findAllByTableGroupId(tableGroupId));

        validateCompletion(orderTables.getIds());

        saveOrderTables(orderTables, null, false);
    }

    private List<OrderTable> saveOrderTables(
        final List<OrderTable> orderTables, final Long tableGroupId
    ) {
        return orderTables.stream()
            .map(it -> new OrderTable(it.getId(), tableGroupId, it.getNumberOfGuests(),
                it.isEmpty()))
            .map(orderTableDao::save)
            .collect(Collectors.toList());
    }

    private void saveOrderTables(
        final OrderTables orderTables, final Long tableGroupId, final boolean empty
    ) {
        orderTables.getOrderTables()
            .stream()
            .map(it -> new OrderTable(it.getId(), tableGroupId, it.getNumberOfGuests(), empty))
            .forEach(orderTableDao::save);
    }

    private void validateCompletion(final List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
