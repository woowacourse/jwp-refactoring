package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.tableGroup.AddOrderTableToTableGroupRequest;
import kitchenpos.dto.request.tableGroup.CreateTableGroupRequest;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private static final List<String> ORDER_STATUS_FOR_CANT_UNGROUP = new ArrayList<String>() {{
        add(OrderStatus.COOKING.name());
        add(OrderStatus.MEAL.name());
    }};

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final CreateTableGroupRequest request) {
        return tableGroupRepository.save(new TableGroup(
            toEntities(request.getOrderTables())
        ));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findByTableGroupId(tableGroupId);
        validateAbleToUngroup(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    private OrderTable findOrderTableById(final Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 테이블입니다."));
    }

    private void validateAbleToUngroup(final List<OrderTable> orderTables) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(orderTables, ORDER_STATUS_FOR_CANT_UNGROUP)) {
            throw new IllegalArgumentException("주문이 시작되어 그룹을 해제할 수 없습니다.");
        }
    }

    private List<OrderTable> toEntities(List<AddOrderTableToTableGroupRequest> orderTables) {
        final List<OrderTable> entities = new ArrayList<>();
        for (AddOrderTableToTableGroupRequest orderTable : orderTables) {
            entities.add(findOrderTableById(orderTable.getId()));
        }

        return entities;
    }
}
