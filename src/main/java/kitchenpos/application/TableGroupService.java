package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.dto.request.TableCreateDto;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private static final String TABLE_GROUP_CREATE_ERROR_MESSAGE = "단체 지정시 개별 주문테이블은 최소 2개 이상이어야 합니다.";
    private static final String UNGROUP_ERROR_MESSAGE = "조리 또는 식사중인 주문은 단체지정을 해제할 수 없습니다.";

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupCreateRequest request) {
        validateTableCount(request);
        final List<OrderTable> savedOrderTables = findOrderTable(request.getOrderTables());

        validateCanGroup(savedOrderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), savedOrderTables));

        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.group(tableGroupId);
        }

        return savedTableGroup;
    }

    private void validateCanGroup(final List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.validateCanGroup();
        }
    }

    private void validateTableCount(final TableGroupCreateRequest request) {
        final List<TableCreateDto> orderTables = request.getOrderTables();
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException(TABLE_GROUP_CREATE_ERROR_MESSAGE);
        }
    }

    private List<OrderTable> findOrderTable(final List<TableCreateDto> orderTables) {
        final List<Long> tableIds = orderTables.stream()
                .map(TableCreateDto::getId)
                .collect(Collectors.toList());
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllById(tableIds);
        validateExistTable(orderTables, savedOrderTables);

        return savedOrderTables;

    }

    private void validateExistTable(final List<TableCreateDto> orderTables,
                                    final List<OrderTable> savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("단체 지정시 개별 주문테이블은 존재해야 합니다.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        validateCanUngroup(orderTableIds);
        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    private void validateCanUngroup(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException(UNGROUP_ERROR_MESSAGE);
        }
    }
}
