package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.request.OrderTableEmptyChangeRequest;
import kitchenpos.dto.request.OrderTableGuestChangeRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableCreateRequest request) {
        return OrderTableResponse.of(orderTableRepository.save(
            new OrderTable(null, null, request.getNumberOfGuests(), request.isEmpty())));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return OrderTableResponse.of(orderTableRepository.findAll());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableEmptyChangeRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("orderTable 을 찾을 수 없습니다."));

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리 중이거나 식사 중인 주문이 있다면 빈 테이블로 만들 수 없습니다.");
        }

        orderTable.changeEmpty(request.isEmpty());

        return OrderTableResponse.of(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
                                                   final OrderTableGuestChangeRequest request) {
        final int numberOfGuests = request.getNumbersOfGuest();

        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        orderTable.changeNumbersOfGuests(numberOfGuests);

        return OrderTableResponse.of(orderTable);
    }
}
