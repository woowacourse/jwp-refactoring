package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.request.TableGroupRequest.TableId;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
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
    public TableGroupResponse create(final TableGroupRequest request) {
        List<OrderTable> savedOrderTables = request.getOrderTables()
                .stream()
                .map(this::getOrderTable)
                .collect(Collectors.toList());

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(savedOrderTables));
        return new TableGroupResponse(savedTableGroup);
    }

    private OrderTable getOrderTable(final TableId tableId) {
        return orderTableRepository.findById(tableId.getId())
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        for (OrderTable orderTable : orderTables) {
            if (orderTable.hasNotCompletedOrder()) {
                throw new IllegalArgumentException();
            }

            orderTable.changeTableGroup(null);
            orderTable.changeEmpty(false);
        }
    }
}
