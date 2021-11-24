package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableValidator;
import kitchenpos.table.ui.request.ChangeTableEmptyRequest;
import kitchenpos.table.ui.request.ChangeTableGuestRequest;
import kitchenpos.table.ui.request.CreateTableRequest;
import kitchenpos.table.ui.response.TableResponse;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableService(
            final OrderTableRepository orderTableRepository,
            final TableValidator tableValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableResponse create(final CreateTableRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        return TableResponse.from(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<TableResponse> list() {
        return orderTableRepository.findAll()
                                   .stream()
                                   .map(TableResponse::from)
                                   .collect(Collectors.toList());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final ChangeTableEmptyRequest request) {
        final OrderTable table = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        table.changeEmpty(request.isEmpty(), tableValidator);
        return TableResponse.from(table);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final ChangeTableGuestRequest request) {
        final OrderTable table = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        table.changeNumberOfGuests(request.getNumberOfGuests());
        return TableResponse.from(table);
    }
}
