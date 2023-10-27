package kitchenpos.tablegroup;

import kitchenpos.ordertable.NoOngoingOrderValidator;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTableRepository;
import kitchenpos.tablegroup.dto.OrderTableRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final NoOngoingOrderValidator noOngoingOrderValidator;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository, NoOngoingOrderValidator noOngoingOrderValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.noOngoingOrderValidator = noOngoingOrderValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroup) {
        TableGroup newTableGroup = tableGroupRepository.save(new TableGroup());

        newTableGroup.add(toOrderTables(tableGroup.getOrderTables()));

        return TableGroupResponse.from(newTableGroup, toOrderTables(tableGroup.getOrderTables()));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.getBy(tableGroupId);
        tableGroup.ungroupUsing(noOngoingOrderValidator);
    }

    private List<OrderTable> toOrderTables(List<OrderTableRequest> orderTableRequests) {
        final List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
        return orderTableRepository.findAllById(orderTableIds);
    }
}
