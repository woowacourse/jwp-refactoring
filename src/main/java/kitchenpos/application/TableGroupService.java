package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.dto.request.TableIdRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository,
                             OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(TableGroupCreateRequest request) {
        List<TableIdRequest> orderTablesRequest = request.getOrderTables();
        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(toTableRequestIds(orderTablesRequest));

        TableGroup tableGroup = request.toEntity(savedOrderTables, savedOrderTables.size());
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupResponse.from(savedTableGroup);
    }

    private List<Long> toTableRequestIds(List<TableIdRequest> orderTablesRequest) { // DTO에서
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
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
