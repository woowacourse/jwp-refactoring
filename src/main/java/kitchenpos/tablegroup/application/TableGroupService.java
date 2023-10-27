package kitchenpos.tablegroup.application;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
import kitchenpos.tablegroup.domain.validator.TableGroupValidator;
import kitchenpos.tablegroup.ui.request.TableGroupRequest;
import kitchenpos.tablegroup.ui.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final TableGroupValidator tableGroupValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> findOrderTables = findOrderTables(tableGroupRequest);
        final TableGroup tableGroup = TableGroup.create(findOrderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.from(savedTableGroup);
    }

    private List<OrderTable> findOrderTables(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        final List<OrderTable> findOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateExistAllOrderTables(orderTableIds, findOrderTables);
        return findOrderTables;
    }

    private void validateExistAllOrderTables(final List<Long> orderTableIds,
                                             final List<OrderTable> findOrderTables) {
        if (orderTableIds.size() != findOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
        tableGroupValidator.validateCompletedTableGroup(tableGroup);
        tableGroup.ungroup();
    }
}
