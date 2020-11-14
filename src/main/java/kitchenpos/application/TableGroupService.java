package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.tablegroup.TableGroupCreateRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.exception.InvalidOrderTableException;
import kitchenpos.exception.InvalidOrderTableIdsException;
import kitchenpos.repository.OrderRepository;
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
    private static final int MIN_COUNT_OF_ORDER_TABLE = 2;

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
    public TableGroupResponse create(TableGroupCreateRequest tableGroupCreateRequest) {
        List<Long> orderTableIds = tableGroupCreateRequest.getOrderTableIds();
        validateOrderTableIds(orderTableIds);

        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateOrderTables(orderTableIds, savedOrderTables);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        for (OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroup(savedTableGroup);
            savedOrderTable.setEmpty(false);
        }
        savedTableGroup.setOrderTables(savedOrderTables);

        return TableGroupResponse.from(savedTableGroup);
    }

    private void validateOrderTableIds(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < MIN_COUNT_OF_ORDER_TABLE) {
            throw new InvalidOrderTableIdsException("단체 지정 생성 시 소속된 주문 테이블이 " + MIN_COUNT_OF_ORDER_TABLE + "개 이상이어야 " +
                                                            "합니다!");
        }
    }

    private void validateOrderTables(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new InvalidOrderTableIdsException("단체 지정 생성 시 소속된 주문 테이블은 모두 존재해야 합니다!");
        }

        for (OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new InvalidOrderTableException("단체 지정 생성 시 소속된 주문 테이블은 주문을 등록할 수 없으며(빈 테이블) 다른 단체 지정이 " +
                                                             "존재해서는 안됩니다!");
            }
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidOrderTableException("단체 지정 제거 시 소속된 주문 테이블의 주문 상태는 조리 혹은 식사가 아니어야 합니다!");
        }

        for (OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(null);
            orderTable.setEmpty(false);
        }
    }
}
