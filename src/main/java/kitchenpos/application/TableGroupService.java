package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.request.TableGroupRequest;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = toOrderTables(tableGroupRequest.getOrderTables());

        validateOrderTables(orderTables, tableGroupRequest.getOrderTables());
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        for (final OrderTable savedOrderTable : orderTables) {
            savedOrderTable.full();
        }

        return savedTableGroup;
    }

    private void validateOrderTables(List<OrderTable> orderTables, List<OrderTableRequest> requests) {
        validateMoreThanTwoElements(orderTables);
        validateNotDuplicatedOrNotExist(orderTables, requests);
        validateIsEmptyAndNotContainTableGroup(orderTables);
    }

    private void validateIsEmptyAndNotContainTableGroup(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            validateOrderTableIsEmptyAndNotContainTableGroup(orderTable);
        }
    }

    private void validateOrderTableIsEmptyAndNotContainTableGroup(OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException();
        }
    }

    private void validateNotDuplicatedOrNotExist(List<OrderTable> orderTables, List<OrderTableRequest> requests) {
        if (orderTables.size() != requests.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMoreThanTwoElements(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderTable> toOrderTables(List<OrderTableRequest> orderTableRequests) {
        return orderTableRequests.stream()
                .map(orderTableRequest -> orderTableRepository.findById(orderTableRequest.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.findByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)).size() > 0) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            tableGroup.deleteOrderTable(orderTable);
            orderTable.deleteTableGroup();
            orderTable.empty();
        }
    }
}
