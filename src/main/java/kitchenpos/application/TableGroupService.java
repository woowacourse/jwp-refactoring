package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private static final int MIN_TABLE_SIZE = 2;

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateRequestOrderTable(orderTableIds, orderTables);
        TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.create());
        List<OrderTable> groupedTables = orderTables.stream()
                .map(orderTable -> orderTable.group(savedTableGroup))
                .collect(Collectors.toList());
        orderTableRepository.saveAll(groupedTables);
        return savedTableGroup;
    }

    private void validateRequestOrderTable(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < MIN_TABLE_SIZE) {
            throw new IllegalArgumentException("table group에 속한 테이블은 2개 이상이여야 합니다.");
        }
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("요청한 Table 중 DB에 저장되어있지 않은 Table이 있습니다.");
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        validateOrderStatusCompletion(orderTableIds);
        List<OrderTable> ungroupedTables = orderTables.stream()
                .map(orderTable -> orderTable.unGroup())
                .collect(Collectors.toList());
        orderTableRepository.saveAll(ungroupedTables);
    }

    private void validateOrderStatusCompletion(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리중이나 식사중일 땐 테이블 그룹 해제가 불가능합니다.");
        }
    }

}
