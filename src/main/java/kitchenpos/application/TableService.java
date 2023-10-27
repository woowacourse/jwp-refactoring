package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.table.TableEmptyChangeRequest;
import kitchenpos.application.dto.table.TableGuestChangeRequest;
import kitchenpos.application.dto.table.TableRequest;
import kitchenpos.application.dto.table.TableResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableChangeEmptyValidator;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableChangeEmptyValidator emptyChangeValidator;

    public TableService(final OrderTableRepository orderTableRepository,
                        final OrderTableChangeEmptyValidator emptyChangeValidator) {
        this.emptyChangeValidator = emptyChangeValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public TableResponse create(final TableRequest orderTableRequest) {
        final OrderTable orderTable = new OrderTable(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
        orderTableRepository.save(orderTable);

        return TableResponse.from(orderTable);
    }

    @Transactional(readOnly = true)
    public List<TableResponse> list() {
        final List<OrderTable> orderTables = orderTableRepository.findAll();

        return orderTables.stream()
            .map(TableResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final TableEmptyChangeRequest request) {
        final OrderTable orderTable = findById(orderTableId);
        emptyChangeValidator.validate(orderTable);
        orderTable.updateEmpty(request.isEmpty());

        return TableResponse.from(orderTable);
    }

    private OrderTable findById(final Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableGuestChangeRequest request) {
        final OrderTable orderTable = findById(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return TableResponse.from(orderTable);
    }
}
