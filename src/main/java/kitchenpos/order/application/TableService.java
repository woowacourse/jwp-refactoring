package kitchenpos.order.application;


import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import kitchenpos.common.exception.CustomErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.repository.TableRepository;
import kitchenpos.order.ui.dto.TableChangeEmptyRequest;
import kitchenpos.order.ui.dto.TableChangeGuestNumberRequest;
import kitchenpos.order.ui.dto.TableCreateRequest;
import kitchenpos.order.ui.dto.TableResponse;
import org.springframework.stereotype.Service;

@Service
public class TableService {

    private final TableRepository tableRepository;

    public TableService(final TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @Transactional
    public TableResponse create(final TableCreateRequest request) {
        return TableResponse.from(tableRepository.save(request.toTable()));
    }

    public List<TableResponse> list() {
        return tableRepository.findAll()
                .stream()
                .map(TableResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public TableResponse changeEmpty(final Long tableId, final TableChangeEmptyRequest request) {
        final OrderTable orderTable = findTableById(tableId);
        orderTable.changeEmpty(request.getEmpty());
        return TableResponse.from(orderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long tableId, final TableChangeGuestNumberRequest request) {
        final OrderTable orderTable = findTableById(tableId);
        orderTable.changeGuestNumber(request.getNumberOfGuests());
        return TableResponse.from(orderTable);
    }

    private OrderTable findTableById(final Long tableId) {
        return tableRepository.findById(tableId)
                .orElseThrow(() -> new NotFoundException(CustomErrorCode.TABLE_NOT_FOUND_ERROR));
    }
}
