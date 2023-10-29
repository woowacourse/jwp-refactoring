package kitchenpos.tablegroup.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.application.dto.TableGroupDto;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.application.dto.TableGroupCreateDto;
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

        final List<OrderTable> orderTables = getOrderTables(request);
        orderTables.forEach(table -> table.group(tableGroup.getId()));

        return TableGroupDto.toDto(tableGroup, orderTables);
    }

    private List<OrderTable> getOrderTables(final TableGroupCreateDto request) {
        final List<Long> orderTableIds = request.getOrderTables()
                .stream()
                .map(TableIdDto::getId)
                .collect(Collectors.toList());
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateOrderTables(orderTableIds, orderTables);

        return orderTables;
    }

    private void validateOrderTables(final List<Long> orderTableIds, final List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블이 포함되어 있습니다.");
        }
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("묶으려는 테이블은 2개 이상이어야 합니다.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        validateOrderStatus(orderTables);

        orderTables.forEach(OrderTable::unGroup);
    }

    private void validateOrderStatus(final List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.MEAL, OrderStatus.COOKING))) {
            throw new IllegalArgumentException("완료되지 않은 주문입니다.");
        }
    }
}
