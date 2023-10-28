package kitchenpos.application;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.dto.ChangeOrderTableEmptyRequest;
import kitchenpos.dto.CreateOrderTableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        final OrderTable orderTable = request.toOrderTable();

        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final ChangeOrderTableEmptyRequest request) {
        final OrderTable savedOrderTable =
                orderTableRepository.findById(orderTableId)
                                    .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        validateCanChangeEmpty(orderTableId);

        savedOrderTable.changeEmpty(request.isEmpty());

        return savedOrderTable;
    }

    private void validateCanChangeEmpty(final Long orderTableId) {
        if (containsNotCompleteOrder(orderTableId)) {
            throw new IllegalArgumentException("주문 상태가 조리중이거나 식사중인 주문이 남아있다면 테이블 상태를 변경할 수 없습니다.");
        }
    }

    private boolean containsNotCompleteOrder(final Long orderTableId) {
        final List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);

        return orders.stream()
                     .anyMatch(Order::isNotComplete);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestsRequest request) {
        validateNumberOfGuests(request);

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                                   .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
        
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return savedOrderTable;
    }

    private void validateNumberOfGuests(final ChangeNumberOfGuestsRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 음수일 수 없습니다.");
        }
    }
}
