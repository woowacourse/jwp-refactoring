package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.application.request.TableGroupCreateRequest;
import kitchenpos.table.application.response.TableGroupResponse;
import kitchenpos.table.domain.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository,
                             TableGroupValidator tableGroupValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(TableGroupCreateRequest tableGroupCreateRequest) {
        List<Long> tableIds = tableGroupCreateRequest.getOrderTables();
        List<OrderTable> foundOrderTables = orderTableRepository.findAllByIdIn(tableIds);

        tableGroupValidator.validateCreateGroup(foundOrderTables);

        TableGroup tableGroup = TableGroup.builder()
                .orderTables(foundOrderTables)
                .build();
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        tableGroupValidator.validateUnGroup(orderTableIds);

        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
        tableGroupRepository.deleteById(tableGroupId);
    }
}
