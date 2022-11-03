package kitchenpos.table.application;

import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_ORDER_EXCEPTION;
import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_TABLE_EXCEPTION;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.exception.CustomIllegalArgumentException;
import kitchenpos.dao.JpaOrderRepository;
import kitchenpos.table.domain.JpaOrderTableRepository;
import kitchenpos.table.domain.JpaTableGroupRepository;
import kitchenpos.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.application.response.TableGroupResponse;
import kitchenpos.table.ui.request.OrderTableIdRequest;
import kitchenpos.table.ui.request.TableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private final JpaOrderRepository orderRepository;
    private final JpaOrderTableRepository orderTableRepository;
    private final JpaTableGroupRepository tableGroupRepository;

    public TableGroupService(final JpaOrderRepository orderRepository,
                             final JpaOrderTableRepository orderTableRepository,
                             final JpaTableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTable> savedOrderTables = getSavedOrderTables(request.getOrderTables());
        final TableGroup saveTableGroup = tableGroupRepository.save(
                TableGroup.of(LocalDateTime.now(), savedOrderTables));
        return TableGroupResponse.from(saveTableGroup);
    }

    private void validateSize(final List<OrderTableIdRequest> targetOrderTables,
                              final List<OrderTable> savedOrderTables) {
        if (targetOrderTables.size() != savedOrderTables.size()) {
            throw new CustomIllegalArgumentException(NOT_FOUND_TABLE_EXCEPTION);
        }
    }

    private List<OrderTable> getSavedOrderTables(final List<OrderTableIdRequest> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);
        validateSize(orderTables, savedOrderTables);
        return savedOrderTables;
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new CustomIllegalArgumentException(NOT_FOUND_TABLE_EXCEPTION));

        //:todo 더 예쁘게 변경해보기
        validChangeOrderTableStatusCondition(tableGroup.getOrderTables());

        for (final OrderTable orderTable : tableGroup.getOrderTables()) {
            orderTable.clear();
        }
    }

    private void validChangeOrderTableStatusCondition(final List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            final Order order = orderRepository.findById(orderTable.getId())
                    .orElseThrow(() -> new CustomIllegalArgumentException(NOT_FOUND_ORDER_EXCEPTION));
            order.validExistOrderStatus();
        }
    }
}
