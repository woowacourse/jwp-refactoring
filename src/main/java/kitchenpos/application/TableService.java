package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.request.TableRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.application.mapper.TableMapper;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.validator.TableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final TableMapper tableMapper;
    private final TableValidator tableValidator;

    public TableService(OrderTableRepository orderTableRepository,
                        TableMapper tableMapper, TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableMapper = tableMapper;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public OrderTableResponse create(final TableRequest tableRequest) {
        OrderTable orderTable = tableMapper.mapFrom(tableRequest);
        orderTableRepository.save(orderTable);
        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.listOf(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final TableRequest tableRequest) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.updateEmpty(tableValidator, tableRequest.getEmpty());
        return OrderTableResponse.of(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final TableRequest tableRequest) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.updateNumberOfGuests(tableRequest.getNumberOfGuests());
        return OrderTableResponse.of(orderTable);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
