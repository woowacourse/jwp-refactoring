package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.mapper.OrderTableDtoMapper;
import kitchenpos.dto.mapper.OrderTableMapper;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.response.OrderTableResponse;
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
                .orElseThrow(IllegalArgumentException::new);
        validateTableDoesNotHaveCookingOrMealOrder(orderTableId);
        savedOrderTable.changeEmpty(empty);
        return orderTableDtoMapper.toOrderTableResponse(savedOrderTable);
    }

    private void validateTableDoesNotHaveCookingOrMealOrder(final Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, List.of(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final int numberOfGuests) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return orderTableDtoMapper.toOrderTableResponse(savedOrderTable);
    }
}
