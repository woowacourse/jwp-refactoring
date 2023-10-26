package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.TableCreateRequest;
import kitchenpos.application.dto.request.TableUpdateEmptyRequest;
import kitchenpos.application.dto.request.TableUpdateNumberOfGuestsRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableValidator;
import kitchenpos.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(final OrderTableRepository orderTableRepository,
                        final OrderTableValidator orderTableValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public OrderTableResponse create(final TableCreateRequest request) {
        final var orderTable = new OrderTable(request.getNumberOfGuests(), request.isEmpty());
        return OrderTableResponse.toResponse(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(OrderTableResponse::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final TableUpdateEmptyRequest request) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        orderTableValidator.validateChangeEmpty(savedOrderTable);
        savedOrderTable.changeEmpty(request.isEmpty());

        return OrderTableResponse.toResponse(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final TableUpdateNumberOfGuestsRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        orderTableValidator.validateChangeNumberOfGuests(savedOrderTable);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.toResponse(orderTableRepository.save(savedOrderTable));
    }
}
