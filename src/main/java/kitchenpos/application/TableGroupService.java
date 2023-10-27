package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository, final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        final List<OrderTable> orderTables = findAllOrderTableByIdIn(extractOrderTableIds(request.getOrderTables()));
        for (OrderTable orderTable : orderTables) {
            final List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());
            validateComplete(orders);
        }
        OrderTables.from(orderTables)
                .group(tableGroup);
        return TableGroupResponse.of(tableGroupRepository.save(tableGroup), orderTables);
    }

    private List<Long> extractOrderTableIds(List<OrderTableRequest> orderTableRequests) {
        return orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    private List<OrderTable> findAllOrderTableByIdIn(List<Long> orderTableIds) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("잘못된 테이블 정보가 포함되어 있습니다.");
        }
        return orderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findTableGroupById(tableGroupId);
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        for (OrderTable orderTable : orderTables) {
            final List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());
            validateComplete(orders);
            orderTable.unGroup();
        }
        tableGroupRepository.save(tableGroup);
    }

    private TableGroup findTableGroupById(final Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 단체 지정 번호입니다."));
    }

    private void validateComplete(List<Order> orders) {
        if (!orders.stream().allMatch(Order::isCompleted)) {
            throw new IllegalArgumentException("조리, 식사 상태일 때는 빈 테이블로 변경할 수 없습니다.");
        }
    }
}
