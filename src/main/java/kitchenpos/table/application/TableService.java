package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.request.ChangeTableEmptyRequest;
import kitchenpos.table.dto.request.ChangeTableNumberOfGuestsRequest;
import kitchenpos.table.dto.request.TableRequest;
import kitchenpos.table.dto.response.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderStatusValidator orderStatusValidator;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderStatusValidator orderStatusValidator, OrderTableRepository orderTableRepository) {
        this.orderStatusValidator = orderStatusValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public TableResponse create(final TableRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return TableResponse.from(savedOrderTable);
    }

    public List<TableResponse> findAll() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(TableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final ChangeTableEmptyRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));

        if (orderStatusValidator.existsByOrderTableIdAndOrderStatusNotCompletion(orderTableId)) {
            throw new IllegalArgumentException("아직 식사가 완료되지 않은 테이블입나다.");
        }

        orderTable.changeEmpty(request.isEmpty());

        return TableResponse.from(orderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final ChangeTableNumberOfGuestsRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다."));

        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return TableResponse.from(orderTable);
    }
}
