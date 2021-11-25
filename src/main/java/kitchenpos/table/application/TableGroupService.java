package kitchenpos.table.application;

import java.time.LocalDateTime;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.TableValidator;
import kitchenpos.table.ui.dto.request.TableGroupCreateRequest;
import kitchenpos.table.ui.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableValidator tableValidator;

    public TableGroupService(
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository,
            TableValidator tableValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest tableGroupCreateRequest) {
        final OrderTables orderTables = OrderTables.create(tableGroupCreateRequest.getOrderTables());
        final OrderTables savedOrderTables = OrderTables.create(orderTableRepository.findAllByIdIn(orderTables.getOrderTableIds()));
        orderTables.validateSameSize(savedOrderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.create(LocalDateTime.now()));
        savedOrderTables.group(savedTableGroup);

        return TableGroupResponse.create(savedTableGroup, savedOrderTables.getOrderTables());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("tableGroupId : " + tableGroupId + "는 존재하지 않는 테이블그룹입니다."));
        final OrderTables orderTables = OrderTables.create(orderTableRepository.findAllByTableGroup(tableGroup));
        orderTables.ungroup(tableValidator);
    }
}
