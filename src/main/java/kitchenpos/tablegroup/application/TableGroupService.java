package kitchenpos.tablegroup.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.application.dto.TableGroupCreateDto;
import kitchenpos.tablegroup.application.dto.TableGroupDto;
import kitchenpos.tablegroup.application.dto.TableIdDto;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    public TableGroupDto create(final TableGroupCreateDto request) {
        final TableGroup tableGroup = new TableGroup();
        tableGroupRepository.save(tableGroup);

        List<OrderTable> orderTables = getOrderTables(request);
        orderTables.forEach(it -> it.group(tableGroup));

        return TableGroupDto.toDto(tableGroup, orderTables);
    }

    private List<OrderTable> getOrderTables(final TableGroupCreateDto request) {
        final List<Long> orderTableIds = request.getOrderTables()
                .stream()
                .map(TableIdDto::getId)
                .collect(Collectors.toList());
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        return orderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.MEAL, OrderStatus.COOKING))) {
            throw new IllegalArgumentException();
        }

        orderTables.forEach(OrderTable::unGroup);
    }
}
