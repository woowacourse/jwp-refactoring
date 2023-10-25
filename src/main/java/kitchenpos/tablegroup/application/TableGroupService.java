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
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderedTableIds);

        if (orderedTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 주문 테이블이 있습니다.");
        }

        final TableGroup tableGroup = TableGroup.createWithNowCreatedDate();
        tableGroup.addOrderTables(savedOrderTables);
        return tableGroupRepository.save(tableGroup).id();
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<Order> orders = orderRepository.findByOrderTable_TableGroup_Id(tableGroupId);
        orders.forEach(Order::ungroupOrderTable);
    }
}
