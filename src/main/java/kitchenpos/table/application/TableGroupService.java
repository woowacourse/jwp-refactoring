package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {
    private final Validator tableGroupValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final Validator tableGroupValidator,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.tableGroupValidator = tableGroupValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();

        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("테이블이 존재하지 않습니다.");
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || savedOrderTable.isGrouped()) {
                throw new IllegalArgumentException("테이블이 사용중이거나 그룹화되어 있습니다.");
            }
            savedOrderTable.changeEmpty(false);
        }

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);
        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("테이블 그룹이 존재하지 않습니다."));

        final List<OrderTable> orderTables = tableGroup.getOrderTables();
        tableGroupValidator.validateOrdersStatus(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.cancelGroup();
        }
    }
}
