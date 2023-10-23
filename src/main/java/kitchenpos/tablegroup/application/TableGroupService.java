package kitchenpos.tablegroup.application;


import java.util.List;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.tablegroup.application.dto.TableGroupRequest;
import kitchenpos.tablegroup.application.dto.TableGroupResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> savedOrderTables = findOrderTables(tableGroupRequest);
        validateExisted(tableGroupRequest, savedOrderTables);

        TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.create());
        savedTableGroup.addOrderTables(savedOrderTables);
        orderTableRepository.saveAll(savedTableGroup.getOrderTables());

        return TableGroupResponse.from(savedTableGroup);
    }

    private List<OrderTable> findOrderTables(TableGroupRequest tableGroupRequest) {
        return orderTableRepository.findAllByIdIn(tableGroupRequest.getOrderTableIds());
    }

    private void validateExisted(TableGroupRequest tableGroupRequest, List<OrderTable> savedOrderTables) {
        if (tableGroupRequest.getOrderTableIds().size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("Some order tables were not found.");
        }
    }

    public void ungroup(final Long tableGroupId) {
        List<OrderTable> orderTable = orderTableRepository.findAllByTableGroupId(tableGroupId);

        orderTable.forEach(OrderTable::ungroup);
        orderTableRepository.saveAll(orderTable);
    }


}
