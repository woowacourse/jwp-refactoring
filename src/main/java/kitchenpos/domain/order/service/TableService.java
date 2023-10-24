package kitchenpos.domain.order.service;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.order.repository.OrderTableRepository;
import kitchenpos.domain.order.service.dto.OrderTableCreateRequest;
import kitchenpos.domain.order.service.dto.OrderTableResponse;
import kitchenpos.domain.order.service.dto.OrderTableUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        final OrderTable toSaveOrderTable = new OrderTable(null, request.getNumberOfGuests(), request.getEmpty());

        final OrderTable savedOrderTable = orderTableRepository.save(toSaveOrderTable);
        return OrderTableResponse.toDto(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::toDto)
                .collect(toList());
    }

    @Transactional
    public void changeEmpty(final Long orderTableId, final OrderTableUpdateRequest reqeust) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("OrderTable 의 식별자로 OrderTable을 찾을 수 없습니다. 식별자 = %s", orderTableId)));
        final Order order = orderRepository.findByOrderTableId(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("OrderTable 의 식별자로 해당하는 Order을 찾을 수 없습니다. 식별자 = %s", orderTableId)));

        if (order.isProceeding()) {
            throw new IllegalArgumentException("Order가 요리 중이거나 식사중이면 상태를 변경할 수 없습니다.");
        }

        savedOrderTable.changeEmpty(reqeust.isEmpty());
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final int numberOfGuests = getNumberOfGuests(orderTable);

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return savedOrderTable;
    }

    private int getNumberOfGuests(final OrderTable orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        return numberOfGuests;
    }
}
