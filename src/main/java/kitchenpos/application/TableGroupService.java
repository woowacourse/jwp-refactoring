package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.application.dto.TableGroupCreateRequest.OrderTableRequest;
import kitchenpos.application.dto.TableGroupResponse;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<OrderTableRequest> requests = request.getOrderTables();

        validateOrderTablesSize(requests);

        final List<OrderTable> orderTables = getOrderTables(requests);

        return TableGroupResponse.of(saveTableGroup(orderTables));
    }

    private void validateOrderTablesSize(final List<OrderTableRequest> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("주문 테이블이 비어 있거나 2개 미만일 수 없습니다.");
        }
    }

    private List<OrderTable> getOrderTables(final List<OrderTableRequest> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        validateMatchingSizes(orderTables, savedOrderTables);

        return savedOrderTables;
    }

    private void validateMatchingSizes(
            final List<OrderTableRequest> orderTables,
            final List<OrderTable> savedOrderTables
    ) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다.");
        }
    }


    private TableGroup saveTableGroup(final List<OrderTable> savedOrderTables) {
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.attachTableGroup(savedTableGroup);
        }

        return savedTableGroup;
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        for (final OrderTable orderTable : orderTables) {
            orderTable.detachTableGroup();
        }
    }
}
