package kitchenpos.order.application;

import kitchenpos.global.exception.KitchenposException;
import kitchenpos.order.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.OrderTableStatusValidator;
import kitchenpos.tablegroup.application.OrderTablesStatusValidator;
import org.springframework.stereotype.Service;

import java.util.List;

import static kitchenpos.global.exception.ExceptionInformation.ORDER_TABLE_STATUS_IS_NOT_COMPLETE;
import static kitchenpos.global.exception.ExceptionInformation.UNGROUP_NOT_COMPLETED_ORDER_TABLE;

@Service
public class OrderTablesStatusValidatorImpl implements OrderTableStatusValidator, OrderTablesStatusValidator {

    private final OrderRepository orderRepository;

    public OrderTablesStatusValidatorImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void validateIsComplete(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, OrderStatus.getNotCompleteStatus())) {
            throw new KitchenposException(ORDER_TABLE_STATUS_IS_NOT_COMPLETE);
        }
    }

    @Override
    public void validateIsComplete(final List<Long> orderTableId) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableId, OrderStatus.getNotCompleteStatus())) {
            throw new KitchenposException(UNGROUP_NOT_COMPLETED_ORDER_TABLE);
        }
    }
}
