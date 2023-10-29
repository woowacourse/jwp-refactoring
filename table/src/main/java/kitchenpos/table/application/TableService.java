package kitchenpos.table.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.table.dto.request.TableCreationRequest;
import kitchenpos.table.dto.request.TableEmptyUpdateRequest;
import kitchenpos.table.dto.request.TableNumberOfGuestsUpdateRequest;
import kitchenpos.table.dto.response.TableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(
            OrderTableRepository orderTableRepository,
            OrderTableValidator orderTableValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public TableResponse create(TableCreationRequest request) {
        OrderTable orderTable = OrderTable.create(request.getNumberOfGuests(), request.getEmpty());

        orderTableRepository.save(orderTable);

        return TableResponse.from(orderTable);
    }

    public List<TableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(TableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TableResponse changeEmpty(Long orderTableId, TableEmptyUpdateRequest request) {
        OrderTable orderTable = findOrderTableById(orderTableId);

        orderTable.changeEmpty(request.getEmpty(), orderTableValidator);

        return TableResponse.from(orderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, TableNumberOfGuestsUpdateRequest request) {
        OrderTable orderTable = findOrderTableById(orderTableId);

        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return TableResponse.from(orderTable);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new NoSuchElementException("ID에 해당하는 주문 테이블이 존재하지 않습니다."));
    }

}
