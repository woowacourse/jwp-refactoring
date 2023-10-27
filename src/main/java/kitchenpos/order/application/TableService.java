package kitchenpos.order.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderTableValidator;
import kitchenpos.order.ui.request.TableCreateRequest;
import kitchenpos.order.ui.request.TableUpdateEmptyRequest;
import kitchenpos.order.ui.request.TableUpdateNumberOfGuestsRequest;
import kitchenpos.order.ui.response.TableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final OrderTableValidator orderTableValidator) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public Long create(final TableCreateRequest request) {
        final OrderTable orderTable = new OrderTable(
                request.getNumberOfGuests(),
                request.getEmpty()
        );

        return orderTableRepository.save(orderTable).getId();
    }

    public List<TableResponse> list() {
        return TableResponse.from(orderTableRepository.findAll());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final TableUpdateEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.changeEmpty(request.getEmpty(), orderTableValidator);
        return TableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableUpdateNumberOfGuestsRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeNumberOfGuests(numberOfGuests, orderTableValidator);
        return TableResponse.from(orderTableRepository.save(savedOrderTable));
    }
}
