package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.application.TableUngroupDomainService;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.exception.CannotUngroupTableException;
import kitchenpos.table.exception.InvalidTableGroupException;
import org.springframework.stereotype.Component;

@Component
public class TableUnDomainServiceImpl implements TableUngroupDomainService {

    private final OrderRepository orderRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableUnDomainServiceImpl(
        OrderRepository orderRepository,
        TableGroupRepository tableGroupRepository
    ) {
        this.orderRepository = orderRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Override
    public void ungroup(Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(InvalidTableGroupException::new);

        List<Order> orders = orderRepository.findByOrderTableIds(tableGroup.orderTableIds());
        if (validateIfCompleteOrders(orders)) {
            throw new CannotUngroupTableException();
        }

        tableGroup.ungroup();
    }

    private boolean validateIfCompleteOrders(List<Order> orders) {
        return orders.stream().anyMatch(Order::isNotCompletion);
    }
}
