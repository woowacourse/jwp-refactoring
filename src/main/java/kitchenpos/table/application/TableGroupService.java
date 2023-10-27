package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.application.dto.request.TableGroupCreateRequest;
import kitchenpos.table.application.dto.response.TableGroupResponse;
import kitchenpos.table.application.mapper.TableGroupMapper;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
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
    public TableGroupResponse create(final TableGroupCreateRequest tableGroupCreateRequest) {
        final OrderTables orderTables = findOrderTables(tableGroupCreateRequest);
        if (orderTables.containEmptyOrderTable()) {
            throw new IllegalArgumentException();
        }
        final TableGroup tableGroup = new TableGroup();
        orderTables.groupAll(tableGroup);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupMapper.mapToResponse(savedTableGroup, orderTables);
    }

    private OrderTables findOrderTables(final TableGroupCreateRequest tableGroupCreateRequest) {
        return new OrderTables(tableGroupCreateRequest.getOrderTables()
                .stream()
                .map(it -> findOrderTableById(it.getId()))
                .collect(Collectors.toList()));
    }

    private OrderTable findOrderTableById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(tableGroup);
        if (containUngroupUnable(orderTables)) {
            throw new IllegalArgumentException();
        }
        for (final OrderTable orderTable : orderTables) {
            orderTable.unGroup();
            orderTableRepository.save(orderTable);
        }
    }

    private boolean containUngroupUnable(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(it -> orderRepository.findAllByOrderTableId(it.getId()))
                .anyMatch(this::containOrderStatusCookingOrMeal);
    }

    private boolean containOrderStatusCookingOrMeal(final List<Order> orders) {
        return orders.stream()
                .anyMatch(it -> it.isOrderStatusEquals(OrderStatus.COOKING)
                        || it.isOrderStatusEquals(OrderStatus.MEAL));

    }
}
