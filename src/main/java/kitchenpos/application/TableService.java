package kitchenpos.application;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.NumberOfGuests;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.ordertable.OrderTableChangeEmptyRequest;
import kitchenpos.dto.ordertable.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.ordertable.OrderTableCreateRequest;
import kitchenpos.dto.ordertable.OrderTableResponse;
import kitchenpos.exception.InvalidOrderTableException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.util.ValidateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse createOrderTable(OrderTableCreateRequest orderTableCreateRequest) {
        OrderTable orderTable = orderTableCreateRequest.toOrderTable();
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> listAllOrderTables() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableChangeEmptyRequest orderTableChangeEmptyRequest) {
        ValidateUtil.validateNonNull(orderTableId);
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new OrderTableNotFoundException(orderTableId));
        validateOrderTable(orderTableId, savedOrderTable);

        savedOrderTable.setEmpty(orderTableChangeEmptyRequest.isEmpty());

        return OrderTableResponse.from(savedOrderTable);
    }

    private void validateOrderTable(Long orderTableId, OrderTable savedOrderTable) {
        if (savedOrderTable.hasTableGroup()) {
            throw new InvalidOrderTableException("주문 등록 가능 여부를 변경하려는 주문 테이블에는 단체 지정이 없어야 합니다!");
        }

        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, OrderStatus.getInProgressStatus())) {
            throw new InvalidOrderTableException("주문 등록 가능 여부를 변경하려는 주문 테이블의 주문 상태는 조리 혹은 식사가 아니어야 합니다!");
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId,
                                                   OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest) {
        ValidateUtil.validateNonNull(orderTableId);
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new OrderTableNotFoundException(orderTableId));

        if (savedOrderTable.isEmpty()) {
            throw new InvalidOrderTableException("방문 손님 수를 변경하려는 주문 테이블은 주문을 등록할 수 있어야(빈 테이블이 아니어야) 합니다!");
        }

        NumberOfGuests numberOfGuests = orderTableChangeNumberOfGuestsRequest.toNumberOfGuests();
        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return OrderTableResponse.from(savedOrderTable);
    }
}
