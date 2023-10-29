package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(TableGroupCreateRequest request) {
        List<Long> orderTableIds = request.getOrderTables()
                .stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
        OrderTables orderTables = new OrderTables(orderTableRepository.getAllByIdIn(orderTableIds));

        TableGroup tableGroup = new TableGroup();
        tableGroupRepository.save(tableGroup);

        orderTables.assignTableGroup(tableGroup.getId());

        return TableGroupResponse.from(tableGroup, orderTables.getOrderTables());
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
        orderTables.ungroup();

        orderTableRepository.saveAll(orderTables.getOrderTables()
                .stream()
                .map(OrderTable::publish)
                .collect(Collectors.toList())
        );
    }
}
