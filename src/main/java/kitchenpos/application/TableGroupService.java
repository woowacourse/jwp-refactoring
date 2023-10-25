package kitchenpos.application;

import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.tablegroup.TableGroupRequest;
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

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(TableGroupRequest tableGroupRequest) {
        final List<Long> orderTableIds = tableGroupRequest.getOrderTables();

        List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);

        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        for (OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroup(savedTableGroup);
        }

        return savedTableGroup;
    }

    @Transactional
    public void ungroup(Long tableGroupId) {

        TableGroup savedTableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        List<Long> orderTableIds = savedTableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리 중이거나 식사 중인 주문 그룹을 해제할 수 없습니다.");
        }

        savedTableGroup.ungroup();
    }
}
