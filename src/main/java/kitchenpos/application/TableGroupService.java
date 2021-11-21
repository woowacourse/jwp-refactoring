package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.exception.CannotUngroupTableException;
import kitchenpos.exception.InvalidOrderTableException;
import kitchenpos.exception.InvalidTableGroupException;
import kitchenpos.exception.InvalidTableGroupSizeException;
import kitchenpos.ui.dto.request.table.TableGroupRequestDto;
import kitchenpos.ui.dto.response.table.OrderTableResponseDto;
import kitchenpos.ui.dto.response.table.TableGroupResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Transactional(readOnly = true)
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
    public TableGroupResponseDto create(final TableGroupRequestDto tableGroupRequestDto) {
        List<Long> orderTableIds = tableGroupRequestDto.getOrderTables();

        if (CollectionUtils.isEmpty(orderTableIds)) {
            throw new InvalidTableGroupSizeException();
        }

        final List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        if (orderTables.size() != orderTableIds.size()) {
            throw new InvalidOrderTableException();
        }

        TableGroup created = tableGroupRepository.save(new TableGroup(orderTables));

        return new TableGroupResponseDto(
            created.getId(),
            created.getCreatedDate(),
            toOrderTableResponseDto(created.getOrderTables())
        );
    }

    private List<OrderTableResponseDto> toOrderTableResponseDto(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(orderTable ->
                new OrderTableResponseDto(
                    orderTable.getId(),
                    orderTable.getTableGroupId(),
                    orderTable.getNumberOfGuests(),
                    orderTable.isEmpty()
                )
            ).collect(toList());
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(InvalidTableGroupException::new);

        List<Order> orders = orderRepository.findOrderByOrderTableIds(tableGroup.orderTableIds());
        if (validateIfCompleteOrders(orders)) {
            throw new CannotUngroupTableException();
        }

        tableGroup.ungroup();
    }

    private boolean validateIfCompleteOrders(List<Order> orders) {
        return orders.stream().anyMatch(Order::isNotCompletion);
    }
}
