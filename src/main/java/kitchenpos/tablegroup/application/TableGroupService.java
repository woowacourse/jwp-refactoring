package kitchenpos.tablegroup.application;

import kitchenpos.exception.NotAllowedUngroupException;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.NotFoundTableGroupException;
import kitchenpos.common.vo.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import kitchenpos.tablegroup.ui.dto.OrderTableDto;
import kitchenpos.tablegroup.ui.dto.TableGroupRequest;
import kitchenpos.tablegroup.ui.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> orderTables = findAllOrderTable(tableGroupRequest.getOrderTables());
        final TableGroup tableGroup = tableGroupRequest.toEntity(LocalDateTime.now(), orderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupResponse.from(savedTableGroup);
    }

    private List<OrderTable> findAllOrderTable(final List<OrderTableDto> orderTableDtos) {
        final List<Long> orderTableIds = orderTableDtos.stream()
                                                       .map(OrderTableDto::getId)
                                                       .collect(Collectors.toList());
        final List<OrderTable> orderTables = findAllOrderTables(orderTableDtos, orderTableIds);

        return orderTables;
    }

    private List<OrderTable> findAllOrderTables(final List<OrderTableDto> orderTableDtos, final List<Long> orderTableIds) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateOrderTables(orderTableDtos, orderTables);
        return orderTables;
    }

    private static void validateOrderTables(final List<OrderTableDto> orderTableDtos, final List<OrderTable> orderTables) {
        if (orderTables.size() != orderTableDtos.size()) {
            throw new NotFoundOrderTableException("존재하지 않는 주문 테이블이 있습니다.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup =
                tableGroupRepository.findById(tableGroupId)
                                    .orElseThrow(() -> new NotFoundTableGroupException("해당 단체 지정이 존재하지 않습니다."));
        final List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                                                   .map(OrderTable::getId)
                                                   .collect(Collectors.toList());
        validateOrdersToUngroup(orderTableIds);

        tableGroup.ungroup();
    }

    private void validateOrdersToUngroup(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))
        ) {
            throw new NotAllowedUngroupException("단체 지정을 해제할 수 없는 주문이 존재합니다.");
        }
    }
}
