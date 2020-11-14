package kitchenpos.application;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.ordertable.OrderTableChangeRequest;
import kitchenpos.dto.ordertable.OrderTableCreateRequest;
import kitchenpos.dto.ordertable.OrderTableResponse;
import kitchenpos.exception.InvalidNumberOfGuestsException;
import kitchenpos.exception.InvalidOrderTableException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableService {
    private static final int MIN_NUMBER_OF_GUESTS = 0;

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableCreateRequest orderTableCreateRequest) {
        int numberOfGuests = orderTableCreateRequest.getNumberOfGuests();
        validateNumberOfGuests(numberOfGuests);

        OrderTable orderTable = new OrderTable(numberOfGuests, orderTableCreateRequest.isEmpty());
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableChangeRequest orderTableChangeRequest) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new OrderTableNotFoundException(orderTableId));
        validateOrderTable(orderTableId, savedOrderTable);

        savedOrderTable.setEmpty(orderTableChangeRequest.isEmpty());

        return OrderTableResponse.from(savedOrderTable);
    }

    private void validateOrderTable(Long orderTableId, OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new InvalidOrderTableException("주문 등록 가능 여부를 변경하려는 주문 테이블에는 단체 지정이 없어야 합니다!");
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new InvalidOrderTableException("주문 등록 가능 여부를 변경하려는 주문 테이블의 주문 상태는 조리 혹은 식사가 아니어야 합니다!");
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableChangeRequest orderTableChangeRequest) {
        int numberOfGuests = orderTableChangeRequest.getNumberOfGuests();
        validateNumberOfGuests(numberOfGuests);

        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new OrderTableNotFoundException(orderTableId));

        if (savedOrderTable.isEmpty()) {
            throw new InvalidOrderTableException("방문 손님 수를 변경하려는 주문 테이블은 주문을 등록할 수 있어야(빈 테이블이 아니어야) 합니다!");
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(savedOrderTable);
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new InvalidNumberOfGuestsException("방문 손님 수는 " + MIN_NUMBER_OF_GUESTS + "명 이상이어야 합니다!");
        }
    }
}
