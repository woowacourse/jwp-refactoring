package kitchenpos.ordertable.service;

import static java.util.stream.Collectors.toList;
import static kitchenpos.exception.ExceptionType.TABLE_GROUP_NOT_FOUND;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.service.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.TableGroup.Builder;
import kitchenpos.exception.CustomException;
import kitchenpos.exception.ExceptionType;
import kitchenpos.ordertable.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderService orderService;
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
        final OrderService orderService,
        final TableService tableService,
        final TableGroupRepository tableGroupRepository
    ) {
        this.orderService = orderService;
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupDto create(final TableGroupDto tableGroupDto) {
        List<OrderTable> orderTables = tableGroupDto.getOrderTables()
                                                    .stream()
                                                    .map(OrderTableDto::getId)
                                                    .map(tableService::findById)
                                                    .collect(toList());

        TableGroup tableGroup = new Builder()
            .createdDate(LocalDateTime.now())
            .orderTables(orderTables)
            .build();

        return TableGroupDto.from(tableGroupRepository.save(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findById(tableGroupId);

        List<Long> ids = getOrderTableIds(tableGroup);

        List<Order> processingOrders = orderService.findByOrderTableIdInAndOrderStatusIn(
            ids,
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)
        );

        if (!processingOrders.isEmpty()) {
            throw new CustomException(ExceptionType.PROCESSING_ORDER_TABLE_CANNOT_UNGROUP);
        }

        tableGroup.ungroup();
    }

    private List<Long> getOrderTableIds(TableGroup tableGroup) {
        return tableGroup.getOrderTables()
                         .stream()
                         .map(OrderTable::getId)
                         .collect(toList());
    }

    public TableGroup findById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                                   .orElseThrow(() -> new CustomException(TABLE_GROUP_NOT_FOUND,
                                String.valueOf(tableGroupId)));
    }
}
