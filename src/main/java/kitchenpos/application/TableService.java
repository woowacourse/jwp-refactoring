package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.request.OrderTableUpdateEmptyRequest;
import kitchenpos.dto.request.OrderTableUpdateNumberOfGuestRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public OrderTableResponse create() {
        OrderTable orderTable = new OrderTable(0, true);
        orderTableRepository.save(orderTable);
        return OrderTableResponse.from(orderTable);
    }

    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableUpdateEmptyRequest orderTableRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다."));

        if (orderTable.isGrouped()) {
            throw new IllegalArgumentException("그룹이 존재하는 테이블은 수정할 수 없습니다.");
        }

        List<Order> orders = orderRepository.findAllByOrderTable(orderTable);
        orders.stream()
                .filter(Order::isProgress)
                .findAny()
                .ifPresent(order -> {
                    throw new IllegalArgumentException("진행중인 주문이 있는 테이블은 수정할 수 없습니다.");
                });

        orderTable.changeEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.from(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(
            Long orderTableId,
            OrderTableUpdateNumberOfGuestRequest orderTableRequest
    ) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다."));

        orderTable.changeNumberOfGuest(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.from(orderTable);
    }
}
