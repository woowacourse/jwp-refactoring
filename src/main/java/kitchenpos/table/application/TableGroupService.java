package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderDto;
import kitchenpos.product.domain.OrderStatus;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableDto;
import kitchenpos.table.dto.TableGroupCreatRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        validateOrderStatus(orderTableIds);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
            orderTableDao.save(orderTable);
        }
    }

    private void validateOrderStatus(final List<Long> orderTableIds) {
        final List<OrderStatus> orderStatuses = orderDao.findAllByOrderTableIdIn(orderTableIds).stream()
                .map(OrderDto::getOrderStatus)
                .map(OrderStatus::valueOf)
                .collect(Collectors.toList());

        for (final OrderStatus status : orderStatuses) {
            if (OrderStatus.COOKING.equals(status) || OrderStatus.MEAL.equals(status)) {
                throw new IllegalArgumentException();
            }
        }
    }
}
