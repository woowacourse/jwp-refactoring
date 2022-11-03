package kitchenpos.billing.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderCompletedEvent;
import kitchenpos.order.domain.OrderProceededEvent;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillingEventHandler {

    private final OrderTableDao orderTableDao;
    private final OrderRepository orderRepository;

    public BillingEventHandler(final OrderTableDao orderTableDao, final OrderRepository orderRepository) {
        this.orderTableDao = orderTableDao;
        this.orderRepository = orderRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void ordered(final OrderProceededEvent event) {
        final OrderTable orderTable = orderTableDao.findById(event.getOrderTableId())
                .orElseThrow(IllegalStateException::new);

        orderTableDao.save(orderTable.order());
    }

    @Async
    @EventListener
    @Transactional
    public void completedOrder(final OrderCompletedEvent event) {
        final OrderTable orderTable = orderTableDao.findById(event.getOrderTableId())
                .orElseThrow();

        final boolean allOrderCompletion = orderRepository.findByOrderTableId(event.getOrderTableId())
                .stream()
                .map(Order::getOrderStatus)
                .allMatch(OrderStatus.COMPLETION::equals);

        if (allOrderCompletion) {
            orderTableDao.save(orderTable.completedOrder());
        }
    }
}
