package kitchenpos.table.domain;

import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class TableValidator {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableValidator(final OrderTableRepository orderTableRepository,
                          final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public void validateTableGroup(final List<Long> tableGroupIds) {
        if (CollectionUtils.isEmpty(tableGroupIds) || tableGroupIds.size() < 2) {
            throw new IllegalArgumentException("주문 테이블이 최소 2개 이상이어야 단체 지정(grouping)이 가능합니다.");
        }
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(tableGroupIds);
        for (OrderTable orderTable : orderTables) {
            validateGroupingTable(orderTable);
        }
    }

    public void validateGroupingTable(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTableRepository.findTableGroupIdById(orderTable.getId()))) {
            throw new IllegalArgumentException("주문 테이블이 비어있지 않거나 이미 단체지정되어있습니다.");
        }
    }
}
