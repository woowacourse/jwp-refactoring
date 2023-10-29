package kitchenpos.order.domain.service;

import java.util.List;
import kitchenpos.order.domain.model.Order;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.model.OrderTable;
import kitchenpos.table.domain.service.TableChangeEmptyValidator;
import org.springframework.stereotype.Service;

@Service
public class TableChangeEmptyOrderValidator implements TableChangeEmptyValidator {

    private final OrderRepository orderRepository;

    public TableChangeEmptyOrderValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validate(OrderTable orderTable) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTable.getId());
        if (!orders.stream().allMatch(Order::isAbleToChangeEmpty)) {
            throw new IllegalArgumentException("완료되지 않은 주문이 있습니다.");
        }
        if (orderTable.getTableGroupId() != null) {
            throw new IllegalArgumentException("이미 단체 지정된 테이블입니다.");
        }
    }
}
