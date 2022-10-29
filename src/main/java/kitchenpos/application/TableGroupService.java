package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.request.tablegroup.OrderTableDto;
import kitchenpos.ui.request.tablegroup.TableGroupCreatRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao,
                             final TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupCreatRequest request) {
        final List<OrderTable> savedOrderTables = mapToOrderTables(request.getOrderTables());
        return tableGroupRepository.save(new TableGroup(LocalDateTime.now(), savedOrderTables));
    }

    private List<OrderTable> mapToOrderTables(final List<OrderTableDto> requests) {
        final List<Long> orderTableIds = requests.stream()
                .map(OrderTableDto::getId)
                .collect(Collectors.toList());
        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);
        if (requests.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
        return savedOrderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        validateOrderStatus(orderDao.findAllByOrderTableIdIn(orderTableIds));

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
            orderTableDao.save(orderTable);
        }
    }

    private void validateOrderStatus(final List<Order> orders) {
        for (Order order : orders) {
            if (order.isStatusCooking() || order.isStatusMeal()) {
                throw new IllegalArgumentException();
            }
        }
    }
}
