package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.application.dto.TableGroupCreateRequest.TableInfo;
import kitchenpos.application.dto.TableGroupResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.OrderTableValidator;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableValidator orderTableValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            OrderTableValidator orderTableValidator,
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository
    ) {
        this.orderTableValidator = orderTableValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(TableGroupCreateRequest request) {
        List<TableInfo> orderTables = request.getOrderTables();
        List<Long> orderTableIds = orderTables.stream()
                .map(TableInfo::getId)
                .collect(Collectors.toList());
        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        return TableGroupResponse.from(tableGroupRepository.save(new TableGroup(savedOrderTables)));
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.getById(tableGroupId);
        tableGroup.ungroup(orderTableValidator);
    }
}
