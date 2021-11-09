package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dtos.OrderTableRequest;
import kitchenpos.application.dtos.TableGroupRequest;
import kitchenpos.application.dtos.TableGroupResponse;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final OrderTables orderTables = new OrderTables(orderTablesWith(request));
        final List<Long> orderTableIds = orderTableIdsWith(request);

        final TableGroup savedTableGroup = tableGroupRepository.save(createTableGroup());
        final OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));
        savedOrderTables.checkValidity(orderTables);
        savedOrderTables.update(savedTableGroup.getId(), false);

        return new TableGroupResponse(savedTableGroup, savedOrderTables.getOrderTables());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));

        final List<Long> orderTableIds = orderTables.getOrderTableIds();
        orderRepository.existsByOrderTableIdIn(orderTableIds);
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        orderTables.update(null, false);
    }

    private TableGroup createTableGroup() {
        return TableGroup.builder()
                .createdDate(LocalDateTime.now())
                .build();
    }

    private List<Long> orderTableIdsWith(TableGroupRequest request) {
        return request.getOrderTables().stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    private List<OrderTable> orderTablesWith(TableGroupRequest request) {
        return request.getOrderTables().stream()
                .map(it -> OrderTable.builder()
                        .id(it.getId())
                        .build())
                .collect(Collectors.toList());
    }

}
