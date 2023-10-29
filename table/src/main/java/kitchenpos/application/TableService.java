package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableValidator;
import kitchenpos.dto.TableCreateRequest;
import kitchenpos.dto.TableEmptyStatusUpdateRequest;
import kitchenpos.dto.TableNumberOfGuestsUpdateRequest;
import kitchenpos.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class TableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(final OrderTableRepository orderTableRepository, final OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    public OrderTable create(final TableCreateRequest request) {
        final OrderTable orderTable = OrderTable.create(request.getNumberOfGuests(), request.isEmpty());
        return orderTableRepository.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    public OrderTable changeEmpty(final Long orderTableId, final TableEmptyStatusUpdateRequest request) {
        final OrderTable orderTable = getOrderTable(orderTableId);

        orderTable.changeEmpty(request.isEmpty(), orderTableValidator);
        return orderTableRepository.save(orderTable);
    }

    public OrderTable changeNumberOfGuests(final Long orderTableId, final TableNumberOfGuestsUpdateRequest request) {
        final OrderTable orderTable = getOrderTable(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return orderTable;
    }

    private OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
    }
}
