package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.OrderTableIdRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderDao orderDao, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTables().stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<OrderTable> savedOrderTables = orderTableIds.stream()
                .map(id -> orderTableRepository.findById(id).orElseThrow(IllegalArgumentException::new))
                .collect(Collectors.toList());

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup().getId())) {
                throw new IllegalArgumentException();
            }
        }

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.updateTableGroup(savedTableGroup);
            savedOrderTable.changeNotEmpty();
        }

        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(savedTableGroup);

        return new TableGroupResponse(
                savedTableGroup.getId(),
                savedTableGroup.getCreatedDate(),
                orderTables.stream()
                        .map(orderTable -> new OrderTableResponse(
                                orderTable.getId(),
                                savedTableGroup.getId(),
                                orderTable.getNumberOfGuests(),
                                orderTable.isEmpty())
                        ).collect(Collectors.toList())
        );
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.updateTableGroup(null);
            orderTable.changeNotEmpty();
        }
    }
}
