package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.domain.TableGroupValidator;
import kitchenpos.ui.request.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository,
            final TableGroupValidator tableGroupValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public Long create(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = request.getOrderTableIds();
        if (orderTableIds.size() < 2) {
            throw new IllegalArgumentException("[ERROR] 2개 이상의 테이블만 그룹으로 묶을 수 있습니다.");
        }

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);

        for (final OrderTable orderTable : savedOrderTables) {
            final Long tableGroupId = orderTable.getTableGroupId();
            if (!orderTable.isEmpty() || Objects.nonNull(tableGroupId)) {
                throw new IllegalArgumentException("[ERROR] 비어있지 않거나, 이미 테이블 그룹이 존재하는 테이블은 그룹에 넣을 수 없습니다.");
            }
        }

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));

        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroupId(tableGroupId);
            savedOrderTable.notEmpty();
            orderTableRepository.save(savedOrderTable);
        }

        return savedTableGroup.getId();
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        orderTables.stream()
                .map(OrderTable::getId)
                .map(it -> orderTableRepository.findById(it).get())
                .forEach(orderTable -> orderTable.ungroup(tableGroupValidator));
    }
}
