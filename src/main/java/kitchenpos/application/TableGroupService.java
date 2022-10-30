package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.request.OrderTableRequest;
import kitchenpos.application.dto.request.TableGroupRequest;
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
        validateNotDuplicatedOrNotExist(orderTables, tableGroupRequest.getOrderTables());
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);

        return tableGroupRepository.save(tableGroup);
    }

    private void validateNotDuplicatedOrNotExist(List<OrderTable> orderTables, List<OrderTableRequest> requests) {
        if (orderTables.size() != requests.size()) {
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
        final List<OrderTable> orderTables = tableGroup.getOrderTables();;

        if (tableGroup.hasOrderTableWhichStatusIsCookingOrMeal()) {
            throw new IllegalArgumentException();
        }

        orderTables.forEach(tableGroup::deleteOrderTable);
    }
}
