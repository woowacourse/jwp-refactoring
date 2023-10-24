package kitchenpos.table.application;

import static kitchenpos.table.domain.exception.TableGroupExceptionType.ORDER_TABLE_IS_NOT_PRESENT_ALL;
import static kitchenpos.table.domain.exception.TableGroupExceptionType.TABLE_GROUP_NOT_FOUND;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.exception.OrderException;
import kitchenpos.order.domain.exception.OrderExceptionType;
import kitchenpos.table.application.dto.OrderTableDto;
import kitchenpos.table.application.dto.TableGroupDto;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.exception.TableGroupException;
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
    public TableGroupDto create(final TableGroupDto tableGroupDto) {
        final List<OrderTableDto> orderTableDtos = tableGroupDto.getOrderTables();
        final List<OrderTable> savedOrderTables = findAndValidateOrderTables(orderTableDtos);

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupDto.from(savedTableGroup);
    }

    private List<OrderTable> findAndValidateOrderTables(final List<OrderTableDto> orderTableDtos) {
        final List<Long> orderTableIds = orderTableDtos.stream()
            .map(OrderTableDto::getId)
            .collect(Collectors.toList());
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableDtos.size() != savedOrderTables.size()) {
            throw new TableGroupException(ORDER_TABLE_IS_NOT_PRESENT_ALL);
        }
        return savedOrderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new TableGroupException(TABLE_GROUP_NOT_FOUND));
        validateContainedTablesOrderStatusIsNotCompletion(tableGroup);
        tableGroup.ungroup();
        tableGroupRepository.delete(tableGroup);
    }

    private void validateContainedTablesOrderStatusIsNotCompletion(final TableGroup tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTables()
            .stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        final boolean orderStatusIsNotCompletion = orderRepository
            .findByOrderTableIdIn(orderTableIds)
            .stream()
            .anyMatch(Order::isNotAlreadyCompletion);
        if (orderStatusIsNotCompletion) {
            throw new OrderException(OrderExceptionType.ORDER_IS_NOT_COMPLETION);
        }
    }
}
