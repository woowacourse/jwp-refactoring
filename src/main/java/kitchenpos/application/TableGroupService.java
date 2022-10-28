package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.dto.request.TableIdRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {
    
    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderDao orderDao,
                             OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(TableGroupCreateRequest request) {
        List<TableIdRequest> orderTablesRequest = request.getOrderTables();
        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(toTableRequestIds(orderTablesRequest));

        TableGroup tableGroup = request.toEntity(savedOrderTables);
        tableGroup.validateOrderTableGrouping(savedOrderTables.size());

        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupResponse.from(savedTableGroup);
    }

    private List<Long> toTableRequestIds(List<TableIdRequest> orderTablesRequest) {
        return orderTablesRequest.stream()
                .map(TableIdRequest::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId);
        validateOrderStatusCompletion(tableGroup.getOrderTableIds());

        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            OrderTable newOrderTable = new OrderTable(orderTable.getId(), null, orderTable.getNumberOfGuests(), false);
            orderTableRepository.update(newOrderTable);
        }
    }

    private void validateOrderStatusCompletion(List<Long> orderTableIds) {
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
