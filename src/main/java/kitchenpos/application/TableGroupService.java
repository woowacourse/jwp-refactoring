package kitchenpos.application;

import kitchenpos.application.dto.request.TableGroupCreateOrderTableRequest;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.application.mapper.TableGroupMapper;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
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
        final TableGroup tableGroup = mapToTableGroup(tableGroupCreateRequest);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupMapper.mapToResponse(savedTableGroup);
    }

    public TableGroup mapToTableGroup(final TableGroupCreateRequest tableGroupCreateRequest) {
        final List<OrderTable> orderTables = tableGroupCreateRequest.getOrderTables()
                .stream()
                .map(this::findOrderTableById)
                .collect(Collectors.toList());
        return new TableGroup(new OrderTables(orderTables));
    }

    private OrderTable findOrderTableById(final TableGroupCreateOrderTableRequest it) {
        return orderTableRepository.findById(it.getId())
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
                .map(orderRepository::findAllByOrderTable)
                .anyMatch(this::containOrderStatusCookingOrMeal);
    }

    private boolean containOrderStatusCookingOrMeal(final List<Order> orders) {
        return orders.stream()
                .anyMatch(it -> it.isOrderStatusEquals(OrderStatus.COOKING)
                        || it.isOrderStatusEquals(OrderStatus.MEAL));

    }
}
