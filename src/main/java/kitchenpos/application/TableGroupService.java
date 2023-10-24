package kitchenpos.application;

import static kitchenpos.exception.OrderTableExceptionType.NOT_EXIST_ORDER_TABLE;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.jpa.JpaOrderRepository;
import kitchenpos.dao.jpa.JpaOrderTableRepository;
import kitchenpos.dao.jpa.JpaTableGroupRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.OrderTableIdRequest;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.exception.OrderTableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final JpaOrderRepository orderRepository;
    private final JpaOrderTableRepository orderTableRepository;
    private final JpaTableGroupRepository tableGroupRepository;

    public TableGroupService(
            JpaOrderRepository orderRepository,
            JpaOrderTableRepository orderTableRepository,
            JpaTableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupCreateRequest request) {
        List<Long> orderTableIds = request.orderTableIds().stream()
                .map(OrderTableIdRequest::id)
                .collect(Collectors.toList());
        List<OrderTable> orderTables = orderTableRepository.getByIdIn(orderTableIds);

        if (orderTableIds.size() != orderTables.size()) {
            throw new OrderTableException(NOT_EXIST_ORDER_TABLE);
        }

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.getReferenceById(tableGroupId);
        List<Order> tablesOrder = orderRepository.getAllByOrderTableIn(tableGroup.orderTables());

        tablesOrder.forEach(Order::ungroupTable);
    }
}
