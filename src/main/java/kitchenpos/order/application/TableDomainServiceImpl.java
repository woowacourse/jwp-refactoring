package kitchenpos.order.application;

import java.util.Arrays;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.application.TableDomainService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TableDomainServiceImpl implements TableDomainService {

    @Autowired
    private final OrderTableRepository orderTableRepository;

    @Autowired
    private final OrderRepository orderRepository;

    public TableDomainServiceImpl(final OrderTableRepository orderTableRepository,
                                  final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public OrderTable changeEmpty(final Long orderTableId, final Boolean empty) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        final OrderTable orderTable = savedOrderTable.updateEmpty(empty);

        return orderTableRepository.save(orderTable);
    }
}
