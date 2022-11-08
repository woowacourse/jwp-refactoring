package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableValidator;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableGroupService(TableGroupRepository tableGroupRepository, OrderTableRepository orderTableRepository,
                             TableValidator tableValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableGroupResponse create(TableGroupCreateRequest request) {
        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(request.toTableRequestIds());
        TableGroup tableGroup = request.toEntity(savedOrderTables, savedOrderTables.size());

        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId);
        tableValidator.ableToUngroup(tableGroup);

        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            OrderTable newOrderTable = new OrderTable(orderTable.getId(), null, orderTable.getNumberOfGuests(), false);
            orderTableRepository.update(newOrderTable);
        }
    }
}
