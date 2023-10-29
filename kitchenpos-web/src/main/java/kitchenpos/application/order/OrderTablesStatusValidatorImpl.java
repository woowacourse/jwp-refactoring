package kitchenpos.application.order;

import kitchenpos.application.table.OrderTableStatusValidator;
import kitchenpos.application.tablegroup.OrderTablesStatusValidator;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.exception.ExceptionInformation;
import kitchenpos.exception.KitchenposException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderTablesStatusValidatorImpl implements OrderTableStatusValidator, OrderTablesStatusValidator {

    private final OrderRepository orderRepository;

    public OrderTablesStatusValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateIsComplete(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, OrderStatus.getNotCompleteStatus())) {
            throw new KitchenposException(ExceptionInformation.ORDER_TABLE_STATUS_IS_NOT_COMPLETE);
        }
    }

    @Override
    public void validateIsComplete(final List<Long> orderTableId) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableId, OrderStatus.getNotCompleteStatus())) {
            throw new KitchenposException(ExceptionInformation.UNGROUP_NOT_COMPLETED_ORDER_TABLE);
        }
    }
}
