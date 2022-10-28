package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.application.dto.TableGroupOrderTableRequest;
import kitchenpos.application.dto.TableGroupRequest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.OrderTableResponse;
import kitchenpos.ui.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(OrderDao orderDao, OrderTableDao orderTableDao, TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    public TableGroupResponse create(TableGroupRequest tableGroupRequest) {
        List<TableGroupOrderTableRequest> orderTableRequests = tableGroupRequest.getOrderTables();

        if (CollectionUtils.isEmpty(orderTableRequests) || orderTableRequests.size() < 2) {
            throw new IllegalArgumentException();
        }

        List<Long> orderTableIds = orderTableRequests.stream()
                .map(TableGroupOrderTableRequest::getId)
                .collect(Collectors.toList());

        List<OrderTable> orderTables = orderTableDao.findAllByIdIn(orderTableIds);

        if (orderTableRequests.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }

        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), orderTables));

        for (OrderTable orderTable : orderTables) {
            orderTableDao.save(
                    new OrderTable(orderTable.getId(), orderTable.getTableGroupId(), orderTable.getNumberOfGuests(),
                            false));
        }

        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(),
                mapToOrderTableResponses(orderTables));
    }

    private List<OrderTableResponse> mapToOrderTableResponses(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(orderTable -> new OrderTableResponse(orderTable.getId(), orderTable.getTableGroupId(),
                        orderTable.getNumberOfGuests(), orderTable.isEmpty()))
                .collect(Collectors.toList());
    }

    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (OrderTable orderTable : orderTables) {
            orderTableDao.save(
                    new OrderTable(orderTable.getId(), null, orderTable.getNumberOfGuests(), orderTable.isEmpty()));
        }
    }
}
