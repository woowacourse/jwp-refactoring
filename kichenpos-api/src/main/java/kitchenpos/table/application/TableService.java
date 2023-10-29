package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableValidator;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.ChangeNumberOfGuestsDto;
import kitchenpos.table.dto.ChangeOrderTableEmptyDto;
import kitchenpos.table.dto.CreateOrderTableDto;
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
    public OrderTable create(final CreateOrderTableDto request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());

        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final ChangeOrderTableEmptyDto request) {
        final OrderTable savedOrderTable =
                orderTableRepository.findById(orderTableId)
                                    .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        savedOrderTable.changeEmpty(request.isEmpty(), orderTableValidator);

        return savedOrderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestsDto request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                                   .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
        
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return savedOrderTable;
    }
}
