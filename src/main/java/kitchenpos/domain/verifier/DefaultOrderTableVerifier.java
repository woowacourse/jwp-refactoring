package kitchenpos.domain.verifier;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.exception.TableGroupSizeException;
import kitchenpos.repository.OrderTableRepository;

@Component
public class DefaultOrderTableVerifier implements OrderTableVerifier{
    private final OrderTableRepository orderTableRepository;

    public DefaultOrderTableVerifier(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public List<OrderTable> verifyOrderTables(List<Long> orderTableIds) {
        if (orderTableIds.size() < TableGroup.MIN_SIZE) {
            throw new TableGroupSizeException(orderTableIds.size());
        }

        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != orderTables.size()) {
            throw new OrderTableNotFoundException();
        }

        return orderTables;
    }
}
