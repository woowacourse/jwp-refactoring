package kitchenpos.application;

import kitchenpos.application.dto.OrderTableDto;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.persistence.OrderRepository;
import kitchenpos.persistence.OrderTableRepository;
import kitchenpos.persistence.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final List<OrderTableDto> request) {
        checkRequestOrderTableSize(request);
        final List<OrderTable> savedOrderTables = findOrderTables(request);
        checkSavedOrderTablesHasSameSizeWithRequest(request, savedOrderTables);

        final OrderTables orderTables = new OrderTables(savedOrderTables);
        final TableGroup tableGroup = new TableGroup(orderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return new TableGroupResponse(savedTableGroup.getId(), savedTableGroup.getCreatedDate(),
                orderTables.getOrderTables()
                        .stream()
                        .map(orderTable -> new OrderTableResponse(orderTable.getId(), savedTableGroup.getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty()))
                        .collect(Collectors.toList()));
    }

    private static void checkRequestOrderTableSize(final List<OrderTableDto> request) {
        if (CollectionUtils.isEmpty(request) || request.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderTable> findOrderTables(List<OrderTableDto> request) {
        final List<Long> orderTableIds = request.stream()
                .map(OrderTableDto::getId)
                .collect(Collectors.toList());

        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    private static void checkSavedOrderTablesHasSameSizeWithRequest(List<OrderTableDto> request, List<OrderTable> savedOrderTables) {
        if (request.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findTableGroupById(tableGroupId);
        validateOrderStatusInTableGroup(tableGroup.getOrderTables());
        tableGroup.ungroup();
        tableGroupRepository.save(tableGroup);
    }

    private TableGroup findTableGroupById(final Long tableGroupId) {
        return tableGroupRepository.findByIdWithOrderTables(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateOrderStatusInTableGroup(final OrderTables orderTables) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
                orderTables.getOrderTables(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
