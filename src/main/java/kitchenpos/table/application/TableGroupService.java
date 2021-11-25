package kitchenpos.table.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.table.exception.TableGroupNotFoundException;
import kitchenpos.table.ui.request.TableGroupRequest;
import kitchenpos.table.ui.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableGroupService(
        TableGroupRepository tableGroupRepository,
        OrderTableRepository orderTableRepository,
        TableValidator tableValidator
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        OrderTables orderTables = generateOrderTables(request.getOrderTables());
        orderTables.validateEmpty();

        TableGroup tableGroup = tableGroupRepository.save(TableGroup.create());
        orderTables.groupBy(tableGroup.getId());

        return TableGroupResponse.from(tableGroup, orderTables.toList());
    }

    private OrderTables generateOrderTables(List<Long> orderTableIds) {
        List<OrderTable> orderTables = new ArrayList<>();

        for (Long orderTableId : orderTableIds) {
            OrderTable orderTable = findOrderTableById(orderTableId);
            orderTables.add(orderTable);
        }

        return new OrderTables(orderTables);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new OrderTableNotFoundException(
                String.format("%s ID에 해당하는 OrderTable이 존재하지 않습니다.", orderTableId)
            ));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findById(tableGroupId);

        for (OrderTable orderTable : orderTableRepository.findAllByTableGroupId(tableGroup.getId())) {
            tableValidator.validateOrders(orderTable.getId());
            orderTable.ungroup();
        }
    }

    public TableGroup findById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new TableGroupNotFoundException(
                String.format("%s ID에 해당하는 TableGroup이 존재하지 않습니다.", tableGroupId)
            ));
    }
}
