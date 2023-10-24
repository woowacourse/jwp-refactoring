package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.domain.vo.OrderStatus;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Transactional
@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(TableGroupCreateRequest tableGroupCreateRequest) {
        List<Long> tableIds = tableGroupCreateRequest.getTableIds();

        if (CollectionUtils.isEmpty(tableIds) || tableIds.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹화를 위해서는 테이블이 2개 이상 필요합니다.");
        }

        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(tableGroupCreateRequest.getTableIds());

        if (orderTables.size() != tableIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블이 존재합니다.");
        }

        for (OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty()) {
                throw new IllegalArgumentException("비어있지 않은 테이블은 그룹화할 수 없습니다.");
            }
            if (orderTable.isGrouped()) {
                throw new IllegalArgumentException("이미 그룹화된 테이블은 그룹화할 수 없습니다.");
            }
        }

        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        tableGroupRepository.save(tableGroup);
        tableGroup.changeOrderTables(orderTables);

        return TableGroupResponse.from(tableGroup);
    }

    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("요리중이거나 식사중인 테이블이 존재합니다.");
        }

        orderTables.forEach(OrderTable::unGroup);
    }
}
