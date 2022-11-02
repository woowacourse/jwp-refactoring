package kitchenpos.application;

import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
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
    public TableGroupResponse create(final TableGroupRequest request) {
        var orderTableIds = request.getOrderTableIds();
        var orderTables = orderTableRepository.findAllById(orderTableIds);

        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블이 포함되어 있습니다. ids = " + orderTableIds);
        }

        return TableGroupResponse.from(
                tableGroupRepository.save(new TableGroup(orderTables))
        );
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        var tableGroup = tableGroupRepository.getById(tableGroupId);
        tableGroup.ungroup();
    }
}
