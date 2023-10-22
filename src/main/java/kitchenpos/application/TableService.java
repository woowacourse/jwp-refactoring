package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다."));

        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException("할당된 그룹이 존재합니다.");
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문이 아직 완료상태가 아닙니다.");
        }

        savedOrderTable.changeEmpty(orderTable.isEmpty());

        return orderTableRepository.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("조정하려는 손님의 수가 0 미만입니다.");
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블의 상태가 비어 있습니다.");
        }
        final OrderTable updatedOrderTable = new OrderTable(
                savedOrderTable.getId(),
                savedOrderTable.getTableGroup(),
                numberOfGuests,
                savedOrderTable.isEmpty()
        );

        return orderTableRepository.save(updatedOrderTable);
    }
}
