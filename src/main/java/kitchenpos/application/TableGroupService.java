package kitchenpos.application;

import static java.util.stream.Collectors.toList;
import static kitchenpos.exception.ExceptionType.TABLE_GROUP_NOT_FOUND;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroup.Builder;
import kitchenpos.dto.OrderTableDto;
import kitchenpos.dto.TableGroupDto;
import kitchenpos.exception.CustomException;
import kitchenpos.exception.ExceptionType;
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
