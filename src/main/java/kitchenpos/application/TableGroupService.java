package kitchenpos.application;

import static kitchenpos.application.exception.ExceptionType.INVALID_TABLE_UNGROUP_EXCEPTION;
import static kitchenpos.application.exception.ExceptionType.NOT_FOUND_TABLE_EXCEPTION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.exception.CustomIllegalArgumentException;
import kitchenpos.dao.JpaOrderTableRepository;
import kitchenpos.dao.JpaTableGroupRepository;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.TableGroupResponse;
import kitchenpos.ui.dto.request.OrderTableIdRequest;
import kitchenpos.ui.dto.request.TableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {
    private final OrderDao orderDao;
    private final JpaOrderTableRepository orderTableRepository;
    private final JpaTableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderDao orderDao, final JpaOrderTableRepository JpaOrderTableRepository,
                             final JpaTableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = JpaOrderTableRepository;
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

    private void validChangeOrderTableStatusCondition(final List<OrderTable> orderTable) {
        final List<Long> orderTableId = orderTable.stream().map(OrderTable::getId).collect(Collectors.toList());
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableId, Arrays.asList(COOKING.name(), MEAL.name()))) {
            throw new CustomIllegalArgumentException(INVALID_TABLE_UNGROUP_EXCEPTION);
        }
    }
}
