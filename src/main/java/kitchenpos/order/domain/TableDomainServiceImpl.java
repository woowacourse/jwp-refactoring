package kitchenpos.order.domain;

import java.util.Arrays;
import kitchenpos.common.DomainService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.TableDomainService;

@DomainService
public class TableDomainServiceImpl implements TableDomainService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableDomainServiceImpl(final OrderTableRepository orderTableRepository,
                                  final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderTable changeEmpty(final Long orderTableId, final boolean empty) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 OrderTable 입니다."));
        validateOrdersCompleted(orderTableId);

        savedOrderTable.changeEmpty(empty);

        return savedOrderTable;
    }

    private void validateOrdersCompleted(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("해당 OrderTable의 Order중 아직 완료되지 않은 것이 존재합니다.");
        }
    }
}
