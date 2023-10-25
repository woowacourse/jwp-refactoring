package kitchenpos.tablegroup.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.event.ValidateAllOrderCompletedEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.request.CreateTableGroupRequest;
import kitchenpos.tablegroup.dto.response.TableGroupResponse;
import kitchenpos.tablegroup.exception.OrderTableCountNotEnoughException;
import kitchenpos.tablegroup.exception.TableGroupNotFoundException;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final ApplicationEventPublisher eventPublisher;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(ApplicationEventPublisher eventPublisher,
            OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.eventPublisher = eventPublisher;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(CreateTableGroupRequest request) {
        List<OrderTable> orderTables = findOrderTables(request.getOrderTables());
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        tableGroupRepository.save(tableGroup);
        groupOrderTables(tableGroup, orderTables);

        return TableGroupResponse.from(tableGroup, orderTables);
    }

    private List<OrderTable> findOrderTables(List<OrderTableRequest> orderTableRequests) {
        return orderTableRequests.stream()
                .map(each -> orderTableRepository.findById(each.getId())
                        .orElseThrow(OrderTableNotFoundException::new))
                .collect(Collectors.toList());
    }

    public void groupOrderTables(TableGroup tableGroup, List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.validateIsEmpty();
            orderTable.validateTableGroupNotExists();

            orderTable.changeEmpty(false);
            orderTable.changeTableGroup(tableGroup.getId());
        }
        validateOrderTableCount(orderTables);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(TableGroupNotFoundException::new);
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());
        validateAllOrdersCompleted(orderTables);
        unGroupOrderTables(orderTables);
    }

    private void validateAllOrdersCompleted(List<OrderTable> orderTables) {
        orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList())
                .forEach(each -> eventPublisher.publishEvent(new ValidateAllOrderCompletedEvent(each)));
    }

    private void validateOrderTableCount(List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new OrderTableCountNotEnoughException();
        }
    }

    public void unGroupOrderTables(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(null);
        }
    }

}
