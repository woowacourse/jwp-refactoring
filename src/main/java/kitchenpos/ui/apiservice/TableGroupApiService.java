package kitchenpos.ui.apiservice;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.OrderTableGroupService;
import kitchenpos.application.OrderTableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableGroup;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupApiService {

    private final OrderTableGroupService orderTableGroupService;
    private final OrderTableService orderTableService;

    public TableGroupApiService(OrderTableGroupService orderTableGroupService,
                                OrderTableService orderTableService) {
        this.orderTableGroupService = orderTableGroupService;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = tableGroupRequest.getOrderTableRequests()
                .stream()
                .map(it -> orderTableService.search(it.getId()))
                .collect(Collectors.toList());
        OrderTableGroup orderTableGroup = orderTableGroupService.create(orderTables);
        return TableGroupResponse.of(orderTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        orderTableGroupService.ungroup(tableGroupId);
    }
}
