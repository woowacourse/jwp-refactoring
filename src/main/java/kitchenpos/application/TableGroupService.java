package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderDetail;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.NotFoundTableGroupException;
import kitchenpos.exception.OrderTableGroupingSizeException;
import kitchenpos.repository.OrderDetailRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.dto.OrderTableIdDto;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderDetailRepository orderDetailRepository;

    public TableGroupService(OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository, OrderDetailRepository orderDetailRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @Transactional
    public TableGroup create(TableGroupCreateRequest tableGroupCreateRequest) {
        validateGroupingSize(tableGroupCreateRequest.getOrderTables());

        List<OrderTable> orderTables = findAllOrderTablesBy(tableGroupCreateRequest.getOrderTables());
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        groupingOrderTables(orderTables, tableGroup);

        return tableGroup;
    }

    private void validateGroupingSize(List<OrderTableIdDto> orderTableIdDtos) {
        if (CollectionUtils.isEmpty(orderTableIdDtos) || orderTableIdDtos.size() < 2) {
            throw new OrderTableGroupingSizeException();
        }
    }

    private List<OrderTable> findAllOrderTablesBy(List<OrderTableIdDto> orderTableIdDtos) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(
                orderTableIdDtos.stream()
                        .map(OrderTableIdDto::getId)
                        .collect(Collectors.toList())
        );
        validateOrderTablesSize(orderTableIdDtos, orderTables);

        return orderTables;
    }

    private void validateOrderTablesSize(List<OrderTableIdDto> orderTableIdDtos,
                                         List<OrderTable> orderTables) {
        if (orderTableIdDtos.size() != orderTables.size()) {
            throw new NotFoundOrderTableException();
        }
    }

    private void groupingOrderTables(List<OrderTable> orderTables, TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            orderTable.grouping(tableGroup);
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        validateOrderTablesGroupId(tableGroupId);
        List<OrderDetail> orderDetails
                = orderDetailRepository.findAllByOrderTableTableGroupId(tableGroupId);

        ungroupingOrderTables(orderDetails);
    }

    private void validateOrderTablesGroupId(Long tableGroupId) {
        if (!tableGroupRepository.existsById(tableGroupId)) {
            throw new NotFoundTableGroupException();
        }
    }

    private void ungroupingOrderTables(List<OrderDetail> orderDetails) {
        for (OrderDetail orderDetail : orderDetails) {
            orderDetail.ungrouping();
        }
    }
}
