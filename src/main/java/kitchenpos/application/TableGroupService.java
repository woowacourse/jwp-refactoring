package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.dto.request.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupCreateRequest request) {
        final List<Long> orderTablesIds = request.getOrderTablesIds();
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTablesIds);

        if (orderTablesIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블이 존재합니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException("주문 테이블이 비어있지 않거나 이미 그룹화된 테이블입니다.");
            }
        }

        TableGroup tableGroup = TableGroup.groupOrderTables(orderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable orderTable : orderTables) {
            orderTable.changeEmpty(false);
            orderTable.setTableGroup(savedTableGroup);
            orderTableRepository.save(orderTable);
        }
        savedTableGroup.setOrderTables(orderTables);
        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문이 완료되지 않은 상태의 테이블이 존재합니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(null);
            orderTable.changeEmpty(false);
            orderTableRepository.save(orderTable);
        }
    }
}
