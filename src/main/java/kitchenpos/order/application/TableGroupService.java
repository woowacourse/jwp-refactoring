package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.domain.TableGroupValidator;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableGroupService {

    private final TableGroupValidator tableGroupValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(TableGroupValidator tableGroupValidator, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.tableGroupValidator = tableGroupValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupRequest request) {
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = orderTableRepository.findAllById(request.getOrderTableIds());
        tableGroup.mapping(orderTables);

        tableGroupValidator.validateGroupCreation(tableGroup, request.getOrderTableIds());
        tableGroupRepository.save(tableGroup);


        return TableGroupResponse.of(tableGroup, orderTables);
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = getTableGroupById(tableGroupId);
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        tableGroupValidator.validateGroupDeletion(orderTables);
        orderTables.forEach(OrderTable::ungroup);
        tableGroupRepository.delete(tableGroup);
    }

    private TableGroup getTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));
    }
}
