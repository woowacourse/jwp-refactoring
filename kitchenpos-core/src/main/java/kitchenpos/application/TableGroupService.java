package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.dto.tablegroup.request.TableGroupCreateRequest;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
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

    @Transactional
    public Long create(final TableGroupCreateRequest request) {
        final List<Long> orderedTableIds = request.orderTableIds();
        validateOrderTableCount(orderedTableIds);

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderedTableIds);
        validateNotExistOrderTable(orderedTableIds, savedOrderTables);
        for (final OrderTable orderTable : savedOrderTables) {
            orderTable.validateNotEmptyOrNonNullTableGroup();
        }

        final TableGroup tableGroup = TableGroup.createWithNowCreatedDate();
        savedOrderTables.forEach(orderTable -> orderTable.group(tableGroup));

        return tableGroupRepository.save(tableGroup).id();
    }

    private void validateOrderTableCount(final List<Long> orderedTableIds) {
        if (CollectionUtils.isEmpty(orderedTableIds) || orderedTableIds.size() < 2) {
            throw new IllegalArgumentException("[ERROR] 주문 테이블이 없거나 2개 미만일 수 없습니다.");
        }
    }

    private void validateNotExistOrderTable(final List<Long> orderedTableIds, final List<OrderTable> savedOrderTables) {
        if (orderedTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블이 있습니다.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<Order> orders = orderRepository.findByOrderTable_TableGroup_Id(tableGroupId);
        orders.forEach(Order::ungroupOrderTable);
    }
}
