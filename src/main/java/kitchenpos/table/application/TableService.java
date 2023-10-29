package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableValidator;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.ChangeOrderTableEmptyRequest;
import kitchenpos.table.dto.CreateOrderTableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {

    private final OrderTableValidator orderTableValidator;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableValidator orderTableValidator, final OrderTableRepository orderTableRepository) {
        this.orderTableValidator = orderTableValidator;
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

        savedOrderTable.changeEmpty(request.isEmpty(), orderTableValidator);

        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestsRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                                   .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
        
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return savedOrderTable;
    }
}
