package kitchenpos.tablegroup.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.model.Order;
import kitchenpos.ordertable.model.OrderTable;
import kitchenpos.order.model.OrderVerifier;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import kitchenpos.tablegroup.model.TableGroup;
import kitchenpos.tablegroup.model.TableGroupVerifier;
import kitchenpos.tablegroup.application.dto.TableGroupCreateRequestDto;
import kitchenpos.tablegroup.application.dto.TableGroupResponseDto;

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
    public TableGroupResponseDto create(final TableGroupCreateRequestDto tableGroupCreateRequestDto) {
        final List<Long> orderTableIds = tableGroupCreateRequestDto.getOrderTableIds();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllById(orderTableIds);

        TableGroupVerifier.validate(orderTableIds, savedOrderTables.size());

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());

        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.group(tableGroupId);
        }
        orderTableRepository.saveAll(savedOrderTables);

        return TableGroupResponseDto.from(savedTableGroup, savedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        final List<Order> orders = orderRepository.findAllByOrderTableIdIn(orderTableIds);
        OrderVerifier.validateNotCompleteOrderStatus(orders);

        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
            orderTableRepository.save(orderTable);
        }
    }
}
