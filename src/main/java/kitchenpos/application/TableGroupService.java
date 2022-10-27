package kitchenpos.application;

import kitchenpos.application.dto.request.SavedOrderTableRequest;
import kitchenpos.application.dto.request.TableGroupRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public TableGroupResponse create(final TableGroupRequest request) {
        final TableGroup tableGroup = convertToTableGroup(request);

        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }

        tableGroup.setCreatedDate(LocalDateTime.now());

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroupId(tableGroupId);
            savedOrderTable.setEmpty(false);
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables);

        return convertToOrderTableResponse(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTable.setEmpty(false);
            orderTableDao.save(orderTable);
        }
    }

    private TableGroup convertToTableGroup(final TableGroupRequest request) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(convertToOrderTables(request.getOrderTables()));
        return tableGroup;
    }

    private List<OrderTable> convertToOrderTables(final List<SavedOrderTableRequest> requests) {
        return requests.stream()
            .map(this::convertToOrderTable)
            .collect(Collectors.toUnmodifiableList());
    }

    private OrderTable convertToOrderTable(final SavedOrderTableRequest request) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(request.getId());
        orderTable.setNumberOfGuests(request.getNumberOfGuests());
        orderTable.setEmpty(request.isEmpty());
        return orderTable;
    }

    private TableGroupResponse convertToOrderTableResponse(final TableGroup orderTable) {
        return new TableGroupResponse(
            orderTable.getId(), orderTable.getCreatedDate(), convertToOrderTableResponses(orderTable.getOrderTables())
        );
    }

    private List<OrderTableResponse> convertToOrderTableResponses(final List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(orderTable -> new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                orderTable.getNumberOfGuests(), orderTable.isEmpty()))
            .collect(Collectors.toUnmodifiableList());
    }
}
