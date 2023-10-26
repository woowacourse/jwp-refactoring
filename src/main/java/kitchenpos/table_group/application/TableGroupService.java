package kitchenpos.table_group.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.dto.OrderTableIdRequest;
import kitchenpos.table_group.application.dto.TableGroupCreateRequest;
import kitchenpos.table_group.application.dto.TableGroupResponse;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.table_group.domain.TableGroup;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.table_group.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = request.getOrderTables().stream().map(OrderTableIdRequest::getId)
            .collect(Collectors.toList());
        validateEmptyOrderTables(orderTableIds);
        final TableGroup tableGroup = TableGroup.forSave();
        final List<OrderTable> savedOrderTables = orderTableRepository.getAllById(orderTableIds);
        final OrderTables orderTables = new OrderTables(savedOrderTables);
        orderTables.group(tableGroup);

        return TableGroupResponse.from(tableGroup, orderTables);
    }

    private void validateEmptyOrderTables(final List<Long> orderTableIds) {
        if (orderTableIds.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 없습니다.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.getById(tableGroupId);
        final List<OrderTable> orderTableGroupedByTableGroup = orderTableRepository.findByTableGroup(tableGroup);
        final OrderTables orderTables = new OrderTables(orderTableGroupedByTableGroup);
        orderTables.ungroup();
    }
}
