package kitchenpos.application.table;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableMapper;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.OrderTableValidator;
import kitchenpos.dto.request.TableCreateRequest;
import kitchenpos.dto.request.TableUpdateEmptyRequest;
import kitchenpos.dto.request.TableUpdateNumberOfGuestsRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;
    private final OrderTableMapper orderTableMapper;

    public TableService(final OrderTableRepository orderTableRepository,
                        final OrderTableValidator orderTableValidator,
                        final OrderTableMapper orderTableMapper) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
        this.orderTableMapper = orderTableMapper;
    }

    @Transactional
    public OrderTableResponse create(final TableCreateRequest request) {
        final var orderTable = orderTableMapper.toOrderTable(request);
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
