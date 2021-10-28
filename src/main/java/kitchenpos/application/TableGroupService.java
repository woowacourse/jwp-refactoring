package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.OrderTables;
import kitchenpos.dto.ordertable.OrderTableRequest;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.exception.InvalidStateException;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
        OrderRepository orderRepository,
        OrderTableRepository orderTableRepository,
        TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final OrderTables orderTables = convertRequestToEntities(tableGroupRequest.getOrderTables());
        orderTables.validate();

        final TableGroup tableGroup = new TableGroup();
        tableGroupRepository.save(tableGroup);

        orderTables.changeAllEmptyToFalse();
        orderTables.assign(tableGroup);

        return TableGroupResponse.of(tableGroup, orderTables.getOrderTables());
    }

    private OrderTables convertRequestToEntities(List<OrderTableRequest> orderTableRequests) {
        final OrderTables orderTables = new OrderTables();
        for (OrderTableRequest orderTableRequest : orderTableRequests) {
            final OrderTable foundOrderTable = findOrderTableById(orderTableRequest.getId());
            orderTables.add(foundOrderTable);
        }
        return orderTables;
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("해당 id의 OrderTable이 없습니다."));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = findOrderTablesByTableGroupId(tableGroupId);
        validateAllOrdersCompleted(orderTables);
        orderTables.ungroup();
    }

    private void validateAllOrdersCompleted(OrderTables orderTables) {
        if (orderRepository.existsByOrderTableIsInAndOrderStatusIsIn(
            orderTables.getOrderTables(), OrderStatus.getExceptCompletion())) {
            throw new InvalidStateException("COMPLETION 상태가 아닌 Order가 존재합니다.");
        }
    }

    private OrderTables findOrderTablesByTableGroupId(Long tableGroupId) {
        final OrderTables orderTables = new OrderTables();
        final List<OrderTable> foundOrderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        for (OrderTable foundOrderTable : foundOrderTables) {
            orderTables.add(foundOrderTable);
        }
        return orderTables;
    }
}
