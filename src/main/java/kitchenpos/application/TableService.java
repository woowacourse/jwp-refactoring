package kitchenpos.application;


import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.TableChangeEmptyRequest;
import kitchenpos.dto.request.TableChangeGuestNumberRequest;
import kitchenpos.dto.request.TableCreateRequest;
import kitchenpos.dto.response.TableResponse;
import kitchenpos.exception.CustomErrorCode;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public TableResponse create(final TableCreateRequest request) {
        return TableResponse.from(orderTableRepository.save(request.toTable()));
    }

    public List<TableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(TableResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public TableResponse changeEmpty(final Long tableId, final TableChangeEmptyRequest request) {
        final OrderTable orderTable = findOrderTableById(tableId);
        orderTable.changeEmpty(request.getEmpty());
        return TableResponse.from(orderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long tableId, final TableChangeGuestNumberRequest request) {
        final OrderTable orderTable = findOrderTableById(tableId);
        orderTable.changeGuestNumber(request.getNumberOfGuests());
        return TableResponse.from(orderTable);
    }

    private OrderTable findOrderTableById(final Long tableId) {
        return orderTableRepository.findById(tableId)
                .orElseThrow(() -> new NotFoundException(CustomErrorCode.TABLE_NOT_FOUND_ERROR));
    }
}
