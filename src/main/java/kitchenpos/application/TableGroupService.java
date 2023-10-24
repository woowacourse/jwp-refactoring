package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
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

    public Long create(final List<Long> orderTableIds) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));
        orderTables.validateSize(orderTableIds.size());
        orderTables.validateIsEmptyAndTableGroupIdIsNull();
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        orderTables.updateTableGroupIdAndEmpty(tableGroup.getId(), false);
        return tableGroup.getId();
    }

    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTables.mapOrderTableIds(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("존재하지 않는 orderTable이거나 table주문 상태가 조리중 또는 식사중인 테이블 그룹은 해체할 수 없습니다.");
        }
        orderTables.updateTableGroupIdAndEmpty(null, false);
    }
}
