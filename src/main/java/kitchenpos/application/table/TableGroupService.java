package kitchenpos.application.table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.common.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.dto.table.mapper.TableGroupDtoMapper;
import kitchenpos.dto.table.request.OrderTableIdRequest;
import kitchenpos.dto.table.request.TableGroupCreateRequest;
import kitchenpos.dto.table.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupDtoMapper tableGroupDtoMapper;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final TableGroupDtoMapper tableGroupDtoMapper, final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.tableGroupDtoMapper = tableGroupDtoMapper;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest tableGroupCreateRequest) {
        List<OrderTable> savedOrderTables = findSavedOrderTables(tableGroupCreateRequest);
        TableGroup savedTableGroup =
                tableGroupRepository.save(new TableGroup(null, LocalDateTime.now(), savedOrderTables));
        return tableGroupDtoMapper.toTableGroupResponse(savedTableGroup);
    }

    private List<OrderTable> findSavedOrderTables(final TableGroupCreateRequest tableGroupCreateRequest) {
        List<Long> orderTableIds = toOrderTableIds(tableGroupCreateRequest.getOrderTables());
        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateOrderTablesExists(orderTableIds, savedOrderTables);
        return savedOrderTables;
    }

    private void validateOrderTablesExists(final List<Long> orderTableIds, final List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private List<Long> toOrderTableIds(final List<OrderTableIdRequest> orderTableIdRequests) {
        return orderTableIdRequests.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
        List<Long> orderTableIds = getOrderTableIds(tableGroup.getOrderTables());
        validateOrderTableNotCompleted(orderTableIds);
        tableGroup.ungroup();
    }

    private List<Long> getOrderTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private void validateOrderTableNotCompleted(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, List.of(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
