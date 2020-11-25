package kitchenpos.application;

import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.OrderTableRepository;
import kitchenpos.dto.order.OrderTableChangeEmptyRequest;
import kitchenpos.dto.order.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.order.OrderTableCreateRequest;
import kitchenpos.dto.order.OrderTableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest request) {
        OrderTable orderTable = request.toOrderTable();
        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        return new OrderTableResponse(savedOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long tableId, final OrderTableChangeEmptyRequest request) {
        if (Objects.isNull(request)) {
            throw new IllegalArgumentException("잘못된 테이블 비우기 요청이 전달되었습니다.");
        }
        final OrderTable savedOrderTable = orderTableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("해당 테이블을 찾을 수 없습니다."));
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                tableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문이 완료되지 않아, 테이블을 비울 수 없습니다.");
        }

        final OrderTable updatedOrderTable = new OrderTable(savedOrderTable.getId(), savedOrderTable.getNumberOfGuests(), request.getEmpty(), savedOrderTable.getTableGroup());

        return new OrderTableResponse(orderTableRepository.save(updatedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long tableId, final OrderTableChangeNumberOfGuestsRequest request) {
        validateNumberOfGuestsRequest(request);
        final OrderTable savedOrderTable = orderTableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("해당 테이블을 찾을 수 없습니다."));
        if (savedOrderTable.getEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블의 인원을 바꿀 수 없습니다.");
        }

        final OrderTable updatedOrderTable = new OrderTable(
                savedOrderTable.getId(),
                request.getNumberOfGuests(),
                savedOrderTable.getEmpty(),
                savedOrderTable.getTableGroup()
        );

        return new OrderTableResponse(orderTableRepository.save(updatedOrderTable));
    }

    private void validateNumberOfGuestsRequest(OrderTableChangeNumberOfGuestsRequest request) {
        if (Objects.isNull(request)) {
            throw new IllegalArgumentException("잘못된 테이블 인원 변경 요청이 전달되었습니다.");
        }
        if (request.getNumberOfGuests() <= 0) {
            throw new IllegalArgumentException("잘못된 인원이 입력되었습니다.");
        }
    }
}
