package kitchenpos.repository.entity;

import java.util.Collection;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.context.annotation.Lazy;

public class OrderTableEntityRepositoryImpl implements OrderTableEntityRepository {

    private final OrderTableRepository orderTableRepository;

    @Lazy
    public OrderTableEntityRepositoryImpl(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public OrderTable getById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다 : " + id));
    }

    @Override
    public List<OrderTable> getAllByIdIn(Collection<Long> ids) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(ids);
        if (ids.size() != orderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블이 있습니다 : " + ids);
        }
        return orderTables;
    }
}
