package kitchenpos.tablegroup.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.request.TableGroupCreateRequest;
import kitchenpos.tablegroup.repository.TableGroupRepository;
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

        final TableGroup tableGroup = TableGroup.createWithNowCreatedDate();
        tableGroup.addOrderTables(savedOrderTables);
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
