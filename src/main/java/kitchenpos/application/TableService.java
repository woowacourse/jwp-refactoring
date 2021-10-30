package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.request.table.ChangeTableEmptyRequest;
import kitchenpos.dto.request.table.ChangeTableGuestRequest;
import kitchenpos.dto.request.table.CreateTableRequest;
import kitchenpos.dto.response.table.TableResponse;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public TableResponse create(final CreateTableRequest request) {
        final OrderTable orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        final OrderTable savedTable = orderTableRepository.save(orderTable);
        return TableResponse.from(savedTable);
    }

    public List<TableResponse> list() {
        return orderTableRepository.findAll().stream()
                                   .map(TableResponse::from)
                                   .collect(Collectors.toList());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final ChangeTableEmptyRequest request) {
        final OrderTable table = orderTableRepository.findById(orderTableId)
                                                     .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        table.changeEmpty(request.isEmpty());
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
