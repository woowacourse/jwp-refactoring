package kitchenpos.application;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableIds;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.dto.tablegroup.TableGroupCreateRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.exception.InvalidOrderTableException;
import kitchenpos.exception.InvalidOrderTableIdsException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.util.ValidateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
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
    public TableGroupResponse createTableGroup(TableGroupCreateRequest tableGroupCreateRequest) {
        OrderTableIds orderTableIds = tableGroupCreateRequest.toOrderTableIds();

        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds.getOrderTableIds());

        if (orderTableIds.isNotEqualSize(savedOrderTables.size())) {
            throw new InvalidOrderTableIdsException("단체 지정 생성 시 소속된 주문 테이블은 모두 존재해야 합니다!");
        }

        TableGroup tableGroup = TableGroup.from(savedOrderTables);
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        savedOrderTables.forEach(savedOrderTable -> {
            savedOrderTable.setTableGroup(savedTableGroup);
            savedOrderTable.changeNotEmpty();
        });

        savedTableGroup.setOrderTables(savedOrderTables);

        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        ValidateUtil.validateNonNull(tableGroupId);
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, OrderStatus.getInProgressStatus())) {
            throw new InvalidOrderTableException("단체 지정 제거 시 소속된 주문 테이블의 주문 상태는 조리 혹은 식사가 아니어야 합니다!");
        }

        orderTables.forEach(OrderTable::ungroup);
    }
}
