package kitchenpos.application;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.ChangeOrderTableEmptyRequest;
import kitchenpos.ui.dto.CreateOrderTableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final CreateOrderTableRequest request) {
        final OrderTable orderTable = request.toEntity();

        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final ChangeOrderTableEmptyRequest request) {
        final OrderTable savedOrderTable = validateCanChangeEmpty(orderTableId);
        savedOrderTable.changeEmpty(request.isEmpty());

        return savedOrderTable;
    }

    private OrderTable validateCanChangeEmpty(final Long orderTableId) {
        final OrderTable savedOrderTable =
                orderTableRepository.findById(orderTableId)
                                    .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))
        ) {
            throw new IllegalArgumentException("주문 상태가 조리중이거나 식사중인 주문이 남아있다면 테이블 상태를 변경할 수 없습니다.");
        }

        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestsRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();
        final OrderTable savedOrderTable = validateCanChangeNumberOfGuests(orderTableId, numberOfGuests);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return savedOrderTable;
    }

    private OrderTable validateCanChangeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 음수일 수 없습니다.");
        }

        return orderTableRepository.findById(orderTableId)
                                   .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
    }
}
