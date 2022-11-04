package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.order.application.dto.request.OrderRequestAssembler;
import kitchenpos.order.application.dto.request.tablegroup.TableGroupRequest;
import kitchenpos.order.application.dto.response.OrderResponseAssembler;
import kitchenpos.order.application.dto.response.TableGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.order.domain.repository.TableGroupRepository;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final OrderRequestAssembler requestAssembler;
    private final OrderResponseAssembler responseAssembler;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
                             final OrderTableRepository orderTableRepository,
                             final OrderRepository orderRepository,
                             final OrderRequestAssembler requestAssembler,
                             final OrderResponseAssembler responseAssembler
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
        this.requestAssembler = requestAssembler;
        this.responseAssembler = responseAssembler;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final var orderTableIds = requestAssembler.asOrderTableIds(request);
        validateOrderTablesMoreThanSingle(orderTableIds);

        final var savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateAllOrderTablesNotDuplicated(orderTableIds, savedOrderTables);
        validateAllOrderTablesNotGrouped(savedOrderTables);

        final var tableGroup = new TableGroup(savedOrderTables);
        final var savedTableGroup = tableGroupRepository.save(tableGroup);
        return responseAssembler.asTableGroupResponse(savedTableGroup);
    }

    private void validateOrderTablesMoreThanSingle(final List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException("2개 이상의 주문 테이블을 지정해야 합니다.");
        }
    }

    private void validateAllOrderTablesNotDuplicated(final List<Long> orderTableIds, final List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("중복되는 주문 테이블이 있습니다.");
        }
    }

    private void validateAllOrderTablesNotGrouped(final List<OrderTable> orderTables) {
        if (isAnyOrderTableAlreadyGrouped(orderTables)) {
            throw new IllegalArgumentException("이미 단체 지정된 주문 테이블입니다.");
        }
    }

    private boolean isAnyOrderTableAlreadyGrouped(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(tableGroupRepository::existsByOrderTableIn);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final var orderTables = asTableGroup(tableGroupId).getOrderTables();
        validateAllOrderTablesCompleted(orderTables);

        tableGroupRepository.removeById(tableGroupId);
    }

    private TableGroup asTableGroup(final Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("단체 지정이 존재하지 않습니다."));
    }

    private void validateAllOrderTablesCompleted(final List<OrderTable> orderTables) {
        final var orderTableIds = asOrderTableIds(orderTables);

        if (existNonCompletedOrderTable(orderTableIds)) {
            throw new IllegalArgumentException("계산이 완료되지 않은 테이블이 존재합니다.");
        }
    }

    private List<Long> asOrderTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private boolean existNonCompletedOrderTable(final List<Long> orderTableIds) {
     return orderRepository.existsByOrderTableIdInAndOrderStatusIn(
             orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL));
    }
}
