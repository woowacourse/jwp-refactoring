package kitchenpos.application;

import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.application.dto.TableGroupResponse;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<OrderTable> orderTables = findOrderTablesIdIn(request.getOrderTables());

        final TableGroup tableGroup = tableGroupRepository.save(
            new TableGroup(LocalDateTime.now(), new OrderTables(orderTables)));

        return TableGroupResponse.createResponse(tableGroup);
    }

    private List<OrderTable> findOrderTablesIdIn(final List<Long> orderTablesIds) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTablesIds);
        if (orderTables.size() != orderTablesIds.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블 입니다.");
        }

        return orderTables;
    }

    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));

        orderTables.unBindGroups();
    }
}
