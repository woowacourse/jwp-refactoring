package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.exception.NotFoundException;
import kitchenpos.ui.dto.TableRequest;
import kitchenpos.ui.dto.TableResponse;
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
    public TableResponse create(final TableRequest tableRequest) {
        return TableResponse.from(orderTableRepository.save(tableRequest.toOrderTable()));
    }

    public List<TableResponse> list() {
        return TableResponse.from(orderTableRepository.findAll());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final TableRequest tableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundException("주문테이블을 찾을 수 없습니다."));

        validateOrderAndOrderStatus(orderTableId);
        savedOrderTable.changeEmpty(tableRequest.isEmpty());
        return TableResponse.from(savedOrderTable);
    }

    private void validateOrderAndOrderStatus(Long orderTableId) {
        List<Order> findOrders = orderRepository.findAllByOrderTableId(orderTableId);
        findOrders.stream()
                .filter(Order::isNotCompleted)
                .findAny()
                .ifPresent(order -> {
                    throw new IllegalArgumentException("조리 혹은 식사 중인 주문이 존재합니다.");
                });
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableRequest tableRequest) {
        int numberOfGuests = tableRequest.getNumberOfGuests();

        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NotFoundException("주문테이블을 찾을 수 없습니다."));

        savedOrderTable.addNumberOfGuests(numberOfGuests);
        return TableResponse.from(savedOrderTable);
    }
}
