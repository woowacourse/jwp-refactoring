package kitchenpos.table;


import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTableRepository;
import kitchenpos.table.ui.TableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class TableGroupService {

    private final TableValidator tableValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final TableValidator tableValidator,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.tableValidator = tableValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest request) {
        final List<Long> requestOrderTableIds = request.getOrderTableIds();
        final List<OrderTable> orderTables = orderTableRepository.findAllById(requestOrderTableIds);

        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        tableGroup.setOrderTables(tableValidator, requestOrderTableIds, orderTables);

        return tableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 단체 지정입니다. 단체 지정을 삭제할 수 없습니다."));
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        tableGroup.unGroup(tableValidator, orderTables);
    }
}
