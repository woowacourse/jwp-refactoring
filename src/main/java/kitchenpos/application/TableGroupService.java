package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.verifier.OrderTableVerifier;
import kitchenpos.domain.verifier.OrderVerifier;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.repository.TableGroupRepository;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderVerifier orderVerifier;
    private final OrderTableVerifier orderTableVerifier;

    public TableGroupService(TableGroupRepository tableGroupRepository, OrderVerifier orderVerifier,
        OrderTableVerifier orderTableVerifier) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderVerifier = orderVerifier;
        this.orderTableVerifier = orderTableVerifier;
    }

    @Transactional
    public TableGroup create(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = request.getOrderTableIds();
        final List<OrderTable> orderTables = orderTableVerifier.verifyOrderTables(orderTableIds);
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        final Long tableGroupId = savedTableGroup.getId();

        for (final OrderTable savedOrderTable : orderTables) {
            savedOrderTable.groupBy(tableGroupId);
        }

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderVerifier
            .verifyOrderStatusByTableGroup(tableGroupId);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }
}
