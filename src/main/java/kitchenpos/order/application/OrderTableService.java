package kitchenpos.order.application;

import java.util.List;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.order.domain.OrderTable;
import org.springframework.stereotype.Service;

@Service
public class OrderTableService {

    private final OrderTableRepository orderTableRepository;

    public OrderTableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void check(Long orderTableId) {
        if (!orderTableRepository.existsById(orderTableId)) {
            throw new IllegalArgumentException("존재하지 않는 orderTable id 입니다.");
        }
    }

    public void checkAll(List<Long> orderTableIds) {
        if (!orderTableRepository.existsByIdIn(orderTableIds)) {
            throw new IllegalArgumentException("존재하지 않는  order table id 입니다");
        }
    }

    public List<OrderTable> findAll(List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }
}
