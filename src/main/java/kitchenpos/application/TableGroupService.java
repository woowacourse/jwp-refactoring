package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.IdRequest;
import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.application.dto.TableGroupResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupVerifier;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupVerifier tableGroupVerifier;

    public TableGroupService(
        final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository,
        final TableGroupVerifier tableGroupVerifier
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupVerifier = tableGroupVerifier;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest tableGroupCreateRequest) {
        final List<Long> orderTableIds = tableGroupCreateRequest.getOrderTables()
            .stream()
            .map(IdRequest::getId)
            .collect(Collectors.toList());

        tableGroupVerifier.verifyOrderTableSize(orderTableIds);

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블은 단체 지정할 수 없습니다.");
        }

        TableGroup savedTableGroup = tableGroupRepository.save(tableGroupCreateRequest.toEntity());

        for (final OrderTable orderTable : savedOrderTables) {
            orderTable.groupBy(savedTableGroup.getId());
        }

        orderTableRepository.saveAll(savedOrderTables);

        return TableGroupResponse.of(savedTableGroup, savedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        tableGroupVerifier.verifyNotCompletedOrderStatus(orderTableIds);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }

        orderTableRepository.saveAll(orderTables);
    }
}
