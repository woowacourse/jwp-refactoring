package kitchenpos.ordertable.application;

import kitchenpos.order.domain.Orders;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.dto.TableGroupCreateRequest;
import kitchenpos.ordertable.dto.TableGroupResponse;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.ordertable.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByIdIn(request.getOrderTableIds()));

        if (!orderTables.isNotSameSizeWith(request.getOrderTableIds().size())) {
            throw new IllegalArgumentException("존재하지 않는 테이블을 단체로 지정할 수 없습니다.");
        }

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        orderTables.groupBy(tableGroup);

        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
        Orders orders = new Orders(orderRepository.findAllByOrderTableIdIn(orderTables.getOrderTableIds()));

        orderTables.ungroup(orders);
    }
}
