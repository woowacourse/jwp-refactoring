package kitchenpos.order.application;

import kitchenpos.exception.NonExistentException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.repository.OrderTableRepository;
import kitchenpos.order.ui.dto.TableRequest;
import kitchenpos.order.ui.dto.TableResponse;
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
                .orElseThrow(() -> new NonExistentException("주문테이블을 찾을 수 없습니다."));

        Orders orders = new Orders(orderRepository.findAllByOrderTableId(orderTableId));
        orders.checkNotCompleted();
        savedOrderTable.changeEmpty(tableRequest.isEmpty());
        return TableResponse.from(savedOrderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableRequest tableRequest) {
        int numberOfGuests = tableRequest.getNumberOfGuests();
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NonExistentException("주문테이블을 찾을 수 없습니다."));
        savedOrderTable.addNumberOfGuests(numberOfGuests);
        return TableResponse.from(savedOrderTable);
    }
}
