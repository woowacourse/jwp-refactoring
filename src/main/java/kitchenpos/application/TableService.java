package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.mapper.OrderTableDtoMapper;
import kitchenpos.dto.mapper.OrderTableMapper;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.response.OrderTableCreateResponse;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.OrderTableUpdateResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableMapper orderTableMapper;
    private final OrderTableDtoMapper orderTableDtoMapper;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderTableMapper orderTableMapper, final OrderTableDtoMapper orderTableDtoMapper,
                        final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderTableMapper = orderTableMapper;
        this.orderTableDtoMapper = orderTableDtoMapper;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableCreateResponse create(final OrderTableCreateRequest orderTableCreateRequest) {
        OrderTable orderTable = orderTableRepository.save(orderTableMapper.toOrderTable(orderTableCreateRequest));
        return orderTableDtoMapper.toOrderTableCreateResponse(orderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTableDtoMapper.toOrderTableResponses(orderTables);
    }

    @Transactional
    public OrderTableUpdateResponse changeEmpty(final Long orderTableId, final boolean empty) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        savedOrderTable.changeEmpty(empty);
        return orderTableDtoMapper.toOrderTableUpdateResponse(savedOrderTable);
    }

    @Transactional
    public OrderTableUpdateResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return orderTableDtoMapper.toOrderTableUpdateResponse(savedOrderTable);
    }
}
