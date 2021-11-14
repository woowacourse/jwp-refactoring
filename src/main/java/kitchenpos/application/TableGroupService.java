package kitchenpos.application;

import java.util.ArrayList;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.TableGroupNotFoundException;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.request.TableGroupRequest;
import kitchenpos.ui.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableService orderTableService;

    public TableGroupService(TableGroupRepository tableGroupRepository, OrderTableService orderTableService) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        OrderTables orderTables = generateOrderTables(request.getOrderTables());
        orderTables.validateEmpty();

        TableGroup tableGroup = tableGroupRepository.save(TableGroup.create());
        orderTables.groupBy(tableGroup);

        return TableGroupResponse.from(tableGroup, orderTables.toList());
    }

    private OrderTables generateOrderTables(List<Long> orderTableIds) {
        List<OrderTable> orderTables = new ArrayList<>();

        for (Long orderTableId : orderTableIds) {
            OrderTable orderTable = orderTableService.findById(orderTableId);
            orderTables.add(orderTable);
        }

        return new OrderTables(orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findById(tableGroupId);
        orderTableService.ungroupWith(tableGroup);
    }

    public TableGroup findById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new TableGroupNotFoundException(
                String.format("%s ID에 해당하는 TableGroup이 존재하지 않습니다.", tableGroupId)
            ));
    }
}
