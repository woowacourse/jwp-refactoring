package kitchenpos.table;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final List<Long> tableIds) {

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(tableIds);

        if (tableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        OrderTables orderTables = new OrderTables(savedOrderTables);
        TableGroup tableGroup = orderTables.group();

        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public TableGroup ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("테이블 그룹이 존재하지 않습니다."));

        tableGroup.unGroup();

        return tableGroup;
    }
}
