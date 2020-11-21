package kitchenpos.domain.verifier;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.exception.TableGroupSizeException;
import kitchenpos.repository.OrderTableRepository;

@Component
public class OrderTableCountVerifier implements CountVerifier<OrderTable>{
    private final OrderTableRepository orderTableRepository;

    public OrderTableCountVerifier(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public List<OrderTable> verify(List<Long> ids) {
        if (ids.size() < TableGroup.MIN_SIZE) {
            throw new TableGroupSizeException(ids.size());
        }

        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(ids);
        if (ids.size() != orderTables.size()) {
            throw new OrderTableNotFoundException();
        }

        return orderTables;
    }
}
