package kitchenpos.order.application;


import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import kitchenpos.common.exception.CustomErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.order.ui.dto.TableChangeEmptyRequest;
import kitchenpos.order.ui.dto.TableChangeGuestNumberRequest;
import kitchenpos.order.ui.dto.TableCreateRequest;
import kitchenpos.order.ui.dto.TableResponse;
import org.springframework.stereotype.Service;

@Service
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
