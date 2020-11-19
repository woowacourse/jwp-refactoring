package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
        List<OrderTableRequest> orderTableRequests = tableGroupRequest.getOrderTables();
        Objects.requireNonNull(orderTableRequests, "테이블 그룹에 속할 테이블 객체가 null 입니다.");
        List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateRequestOrderTable(orderTableRequests, savedOrderTables);
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroupRequest.toTableGroup());
        updateTableGroupOfOrderTables(savedOrderTables, savedTableGroup);
        return savedTableGroup;
    }

    private void validateRequestOrderTable(List<OrderTableRequest> orderTableRequests, List<OrderTable> savedOrderTables) {
        if (CollectionUtils.isEmpty(orderTableRequests) || orderTableRequests.size() < MIN_TABLE_SIZE) {
            throw new IllegalArgumentException("table group에 속한 테이블은 2개 이상이여야 합니다.");
        }
        if (orderTableRequests.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("요청한 Table 중 저장되어있지 않은 Table이 있습니다.");
        }
        for (OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException("이미 테이블 그룹에 속한 테이블이거나 비어있지 않은 테이블입니다.");
            }
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        validateOrderStatusCompletion(orderTableIds);

        updateTableGroupOfOrderTables(orderTables, null);
    }

    private void validateOrderStatusCompletion(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리중이나 식사중일 땐 테이블 그룹 해제가 불가능합니다.");
        }
    }

    private void updateTableGroupOfOrderTables(List<OrderTable> orderTables, TableGroup tableGroup) {
        for (OrderTable savedOrderTable : orderTables) {
            savedOrderTable = savedOrderTable.changeTableGroup(tableGroup);
            savedOrderTable = savedOrderTable.changeEmpty(false);
            orderTableRepository.save(savedOrderTable);
        }
    }
}
