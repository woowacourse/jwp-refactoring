package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.application.request.OrderTableCreateRequest;
import kitchenpos.table.application.request.OrderTableUpdateRequest;
import kitchenpos.table.application.response.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableCreateRequest orderTableCreateRequest) {
        OrderTable orderTable = OrderTable.builder()
                .numberOfGuests(orderTableCreateRequest.getNumberOfGuests())
                .empty(orderTableCreateRequest.isEmpty())
                .build();

        OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableUpdateRequest orderTableUpdateRequest) {
        OrderTable foundOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 주문 테이블이 존재하지 않습니다."));

        if (foundOrderTable.isGrouped()) {
            throw new IllegalArgumentException("주문 그룹에 속한 주문 테이블의 상태를 변경할 수 없습니다.");
        }

        if (!foundOrderTable.isAllOfOrderCompleted()) {
            throw new IllegalArgumentException("테이블에 진행 중인 주문이 존재합니다.");
        }

        foundOrderTable.changeTableStatus(orderTableUpdateRequest.isEmpty());
        return OrderTableResponse.from(foundOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableUpdateRequest orderTableUpdateRequest) {
        OrderTable foundOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 주문 테이블이 존재하지 않습니다."));

        if (foundOrderTable.isEmpty()) {
            throw new IllegalArgumentException("EMPTY 상태인 주문 테이블의 손님 수를 변경할 수 없습니다.");
        }

        foundOrderTable.changeNumberOfGuests(orderTableUpdateRequest.getNumberOfGuests());

        return OrderTableResponse.from(foundOrderTable);
    }
}
