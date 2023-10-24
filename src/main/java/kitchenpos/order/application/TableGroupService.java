package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.presentation.dto.CreateTableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;

    private final TableGroupRepository tableGroupRepository;

    private final OrderRepository orderRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    public TableGroup create(final CreateTableGroupRequest request) {
        final List<OrderTable> orderTables = request.getOrderTables().stream()
                                                    .map(orderTableRequest -> orderTableRepository.findById(orderTableRequest.getId())
                                                                                                  .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다.")))
                                                    .collect(Collectors.toList());

        if (orderTables.isEmpty() || orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹을 만들기 위해선, 적어도 2개 이상의 주문 테이블이 필요합니다.");
        }

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                                                     new ArrayList<>());

        tableGroup.addOrderTables(orderTables);
        return tableGroupRepository.save(tableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                                                    .map(OrderTable::getId)
                                                    .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                                                                   Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("현재 진행중인 주문이 존재하여, 테이블 그룹을 해제할 수 없습니다.");
        }

        orderTables.forEach(OrderTable::ungroup);
    }
}
