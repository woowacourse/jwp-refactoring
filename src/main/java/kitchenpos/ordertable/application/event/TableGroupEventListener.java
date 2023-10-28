package kitchenpos.ordertable.application.event;

import kitchenpos.ordertable.OrderTables;
import kitchenpos.ordertable.application.validator.OrderStatusBatchValidator;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.TableGroup;
import kitchenpos.tablegroup.application.event.dto.TableGroupCreateRequestEvent;
import kitchenpos.tablegroup.application.event.dto.TableGroupDeleteRequestEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class TableGroupEventListener {

    private final OrderTableRepository orderTableRepository;
    private final OrderStatusBatchValidator orderStatusBatchValidator;

    public TableGroupEventListener(
            final OrderTableRepository orderTableRepository,
            final OrderStatusBatchValidator orderStatusBatchValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderStatusBatchValidator = orderStatusBatchValidator;
    }

    @EventListener
    public void handleTableGroupCreationRequested(final TableGroupCreateRequestEvent event) {
        final List<Long> orderTableIds = event.getOrderTableIds();
        final OrderTables orderTables = new OrderTables(orderTableRepository.findAllById(orderTableIds));
        validateAllOrderTablesExist(orderTableIds, orderTables);
        orderTables.assignGroup(event.getTableGroup());
        orderTableRepository.saveAll(orderTables.getOrderTables());
    }

    private void validateAllOrderTablesExist(final List<Long> orderTableIds, final OrderTables searchedOrderTables) {
        if (!searchedOrderTables.hasSizeOf(orderTableIds.size())) {
            throw new IllegalArgumentException();
        }
    }

    @EventListener
    public void handleTableGroupDeletionRequested(final TableGroupDeleteRequestEvent event) {
        final TableGroup tableGroup = event.getTableGroup();
        final OrderTables orderTables = new OrderTables(orderTableRepository.findByTableGroup(tableGroup));
        orderStatusBatchValidator.batchValidateCompletion(orderTables);
        orderTables.ungroup();
        orderTableRepository.saveAll(orderTables.getOrderTables());
    }
}
