package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.*;
import kitchenpos.table.ui.dto.request.TableGroupCreateRequest;
import kitchenpos.table.ui.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
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
    public TableGroupResponse create(final TableGroupCreateRequest tableGroupCreateRequest) {
        final OrderTables orderTables = OrderTables
                .create(tableGroupCreateRequest.getOrderTables());
        final OrderTables savedOrderTables = OrderTables
                .create(orderTableRepository.findAllByIdIn(orderTables.getOrderTableIds()));
        orderTables.validateSameSize(savedOrderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.create(LocalDateTime.now()));
        savedOrderTables.group(savedTableGroup);

        return TableGroupResponse.create(savedTableGroup, savedOrderTables.getOrderTables());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("tableGroupId : " + tableGroupId + "는 존재하지 않는 테이블그룹입니다."));
        final OrderTables orderTables = OrderTables.create(orderTableRepository.findAllByTableGroup(tableGroup));
        for (OrderTable orderTable : orderTables.getOrderTables()) {
            Orders orders = Orders.create(orderRepository.findAllByOrderTable(orderTable));
            orders.validateCompleted();
        }
        orderTables.ungroup();
    }
}
