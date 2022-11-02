package kitchenpos.application.table;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.table.mapper.OrderTableDtoMapper;
import kitchenpos.dto.table.mapper.OrderTableMapper;
import kitchenpos.dto.table.request.OrderTableCreateRequest;
import kitchenpos.dto.table.response.OrderTableResponse;
import kitchenpos.exception.badrequest.OrderNotExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderTableMapper orderTableMapper;
    private final OrderTableDtoMapper orderTableDtoMapper;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableMapper orderTableMapper, final OrderTableDtoMapper orderTableDtoMapper,
                        final OrderTableRepository orderTableRepository) {
        this.orderTableMapper = orderTableMapper;
        this.orderTableDtoMapper = orderTableDtoMapper;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableCreateRequest orderTableCreateRequest) {
        OrderTable orderTable = orderTableRepository.save(orderTableMapper.toOrderTable(orderTableCreateRequest));
        return orderTableDtoMapper.toOrderTableResponse(orderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTableDtoMapper.toOrderTableResponses(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final boolean empty) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderNotExistsException::new);
        savedOrderTable.changeEmpty(empty);
        return orderTableDtoMapper.toOrderTableResponse(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(OrderNotExistsException::new);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return orderTableDtoMapper.toOrderTableResponse(savedOrderTable);
    }
}
