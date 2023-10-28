package kitchenpos.ordertable.service;

import static java.util.stream.Collectors.toList;
import static kitchenpos.exception.ExceptionType.PROCESSING_ORDER_TABLE_CANNOT_UNGROUP;
import static kitchenpos.exception.ExceptionType.TABLE_GROUP_NOT_FOUND;

import java.util.Arrays;
import java.util.List;
import kitchenpos.exception.CustomException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final TableGroupValidator tableGroupValidator;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
        final OrderRepository orderRepository,
        final TableGroupValidator tableGroupValidator,
        final TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.tableGroupValidator = tableGroupValidator;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        tableGroupValidator.validate(tableGroup);

        tableGroup.group();

        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = getById(tableGroupId);

        validateUngroupable(tableGroup);

        tableGroup.ungroup();
    }

    private void validateUngroupable(TableGroup tableGroup) {
        List<Long> ids = getOrderTableIds(tableGroup);

        List<Order> processingOrders = orderRepository.findByOrderTableIdInAndOrderStatusIn(
            ids,
            Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL)
        );

        if (!processingOrders.isEmpty()) {
            throw new CustomException(PROCESSING_ORDER_TABLE_CANNOT_UNGROUP);
        }
    }

    private List<Long> getOrderTableIds(TableGroup tableGroup) {
        return tableGroup.getOrderTables()
                         .stream()
                         .map(OrderTable::getId)
                         .collect(toList());
    }

    public TableGroup getById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                                   .orElseThrow(() -> new CustomException(TABLE_GROUP_NOT_FOUND, String.valueOf(tableGroupId)));
    }
}
