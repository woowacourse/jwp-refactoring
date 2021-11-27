package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.exception.CannotUngroupTableException;
import kitchenpos.exception.InvalidTableGroupException;
import org.springframework.stereotype.Component;

@Component
public class TableUngroupDomainServiceImpl implements TableUngroupDomainService {

    private final OrderRepository orderRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableUngroupDomainServiceImpl(
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
