package kitchenpos.tablegroup.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.request.TableGroupCreationRequest;
import kitchenpos.tablegroup.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
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
    public TableGroupResponse create(TableGroupCreationRequest request) {
        List<Long> orderTableIds = request.getOrderTableIds();
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        validateOrderTableSize(orderTableIds, orderTables);

        TableGroup tableGroup = TableGroup.createWithGrouping(orderTables);
        tableGroupRepository.save(tableGroup);

        return TableGroupResponse.from(tableGroup);
    }

    private void validateOrderTableSize(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블이 존재합니다.");
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        validateEmpty(orderTableIds);

        orderTables.forEach(OrderTable::ungroup);
    }

    private void validateEmpty(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문이 완료되지 않은 상태의 테이블이 존재합니다.");
        }
    }

}
