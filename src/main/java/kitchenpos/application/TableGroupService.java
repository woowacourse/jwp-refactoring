package kitchenpos.application;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderVerifier;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;

@Service
@Validated
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderVerifier orderVerifier;

    public TableGroupService(OrderTableRepository orderTableRepository,
        TableGroupRepository tableGroupRepository, OrderVerifier orderVerifier) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderVerifier = orderVerifier;
    }

    @Transactional
    public TableGroup create(@Valid final TableGroupCreateRequest request) {
        List<Long> orderTableIds = request.getOrderTableIds();

        final List<OrderTable> orderTables = orderTableRepository
            .findAllByIdIn(orderTableIds);

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
