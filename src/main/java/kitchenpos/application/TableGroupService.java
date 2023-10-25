package kitchenpos.application;

import kitchenpos.application.exception.NotAllowedUngroupException;
import kitchenpos.application.exception.NotFoundOrderTableException;
import kitchenpos.application.exception.NotFoundTableGroupException;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.dto.tablegroup.OrderTableDto;
import kitchenpos.ui.dto.tablegroup.TableGroupRequest;
import kitchenpos.ui.dto.tablegroup.TableGroupResponse;
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
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTables.size() != orderTableDtos.size()) {
            throw new NotFoundOrderTableException("존재하지 않는 주문 테이블이 있습니다.");
        }

        return orderTables;
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
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
        ) {
            throw new NotAllowedUngroupException("단체 지정을 해제할 수 없는 주문이 존재합니다.");
        }
    }
}
