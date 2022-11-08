package kitchenpos.ordertable.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.dto.IdRequest;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTables;
import kitchenpos.ordertable.TableGroup;
import kitchenpos.ordertable.dto.TableGroupRequest;
import kitchenpos.ordertable.dto.TableGroupResponse;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.ordertable.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
                             final OrderTableRepository orderTableRepository,
                             final OrderTableValidator orderTableValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<IdRequest> orderTableIdRequests = request.getOrderTables();
        final List<OrderTable> savedOrderTables = getOrderTablesOf(orderTableIdRequests);
        final OrderTables orderTables = new OrderTables(savedOrderTables);

        final TableGroup tableGroup = TableGroup.ofUnsaved();
        tableGroupRepository.save(tableGroup);
        orderTables.joinGroup(tableGroup.getId());

        return TableGroupResponse.from(tableGroup, orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findByTableGroupId(tableGroupId);
        final OrderTables groupedTables = new OrderTables(orderTables);

        final List<Long> orderTableIds = groupedTables.getIds();
        orderTableValidator.checkOrderComplete(orderTableIds);

        groupedTables.ungroup();
    }

    private List<OrderTable> getOrderTablesOf(final List<IdRequest> orderTableIdRequests) {
        return orderTableIdRequests.stream()
                .map(this::getOrderTableFrom)
                .collect(Collectors.toList());
    }

    private OrderTable getOrderTableFrom(final IdRequest request) {
        return orderTableRepository.findById(request.getId())
                .orElseThrow(IllegalArgumentException::new);
    }
}
