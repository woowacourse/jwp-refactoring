package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.OrderTableIdDto;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
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
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final OrderTables orderTables = getOrderTables(request.getOrderTables());
        final TableGroup tableGroup = tableGroupRepository.save(request.toTableGroup());
        orderTables.fillTableGroup(tableGroup);
        orderTableRepository.saveAll(orderTables.getValues());
        return TableGroupResponse.from(tableGroup, orderTables);
    }

    private OrderTables getOrderTables(final List<OrderTableIdDto> orderTableIdDtos) {
        List<OrderTable> orderTables = orderTableRepository.findAllByOrderTableIdsIn(
                extractOrderTableIds(orderTableIdDtos));
        return OrderTables.fromCreate(orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
        validateCompletion(orderTables.getOrderIds());
        orderTables.upgroup();
        orderTableRepository.saveAll(orderTables.getValues());
    }

    private List<Long> extractOrderTableIds(final List<OrderTableIdDto> orderTableIdsDto) {
        return orderTableIdsDto.stream()
                .map(OrderTableIdDto::getId)
                .collect(Collectors.toList());
    }

    private void validateCompletion(final List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
