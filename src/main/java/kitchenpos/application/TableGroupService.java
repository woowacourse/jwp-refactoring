package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.response.OrderTableIdResponse;
import kitchenpos.ui.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(List<Long> orderTableIds) {
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        final List<OrderTable> orderTables = findOrderTablesIdIn(orderTableIds);

        tableGroup.bind(orderTables);

        return tableGroup;
    }

    private List<OrderTable> findOrderTablesIdIn(final List<Long> orderTableIds) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
        return orderTables;
    }

    @Transactional(readOnly = true)
    public TableGroupResponse findById(long tableId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableId)
                .orElseThrow(IllegalArgumentException::new);
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());

        List<OrderTableIdResponse> orderTableIds = orderTables.stream()
                .map(o -> new OrderTableIdResponse(o.getId()))
                .collect(Collectors.toList());
        return new TableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate(), orderTableIds);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        for (OrderTable orderTable : orderTables) {
            orderTable.leaveGroup();
        }
    }
}
