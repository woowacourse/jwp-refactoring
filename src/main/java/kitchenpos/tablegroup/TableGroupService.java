package kitchenpos.tablegroup;

import kitchenpos.ordertable.NoOngoingOrderValidator;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTableRepository;
import kitchenpos.ui.dto.OrderTableRequest;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final NoOngoingOrderValidator noOngoingOrderValidator;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository, NoOngoingOrderValidator noOngoingOrderValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.noOngoingOrderValidator = noOngoingOrderValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroup) {
        List<OrderTable> orderTables = toOrderTables(tableGroup.getOrderTables());
        validateSizeOf(orderTables);
        TableGroup newTableGroup = tableGroupRepository.save(new TableGroup());
        orderTables.forEach(table -> table.assign(newTableGroup.getId()));

        return TableGroupResponse.from(newTableGroup, orderTables);
    }

    private void validateSizeOf(List<OrderTable> orderTables) {
        /*private static */
        final int MINIMUM_GROUP_SIZE = 2;
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_GROUP_SIZE) {
            throw new IllegalArgumentException(MINIMUM_GROUP_SIZE + "개 이상의 테이블이 필요합니다");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        tableGroupRepository.validateContains(tableGroupId);

        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        for (OrderTable orderTable : orderTables) {
            noOngoingOrderValidator.validate(orderTable);
            orderTable.ungroup();
        }
    }

    private List<OrderTable> toOrderTables(List<OrderTableRequest> orderTableRequests) {
        final List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
        return orderTableRepository.findAllById(orderTableIds);
    }
}
