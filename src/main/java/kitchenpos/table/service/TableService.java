package kitchenpos.table.service;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
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
    public TableResponse create(final TableRequest tableRequest) {
        OrderTable orderTable = tableMapper.mapFrom(tableRequest);
        orderTableRepository.save(orderTable);
        return TableResponse.of(orderTable);
    }

    public List<TableResponse> list() {
        return TableResponse.listOf(orderTableRepository.findAll());
    }

    @Transactional
    public TableResponse changeEmpty(final Long orderTableId, final TableRequest tableRequest) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.updateEmpty(tableValidator, tableRequest.getEmpty());
        return TableResponse.of(orderTable);
    }

    @Transactional
    public TableResponse changeNumberOfGuests(final Long orderTableId, final TableRequest tableRequest) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.updateNumberOfGuests(tableRequest.getNumberOfGuests());
        return TableResponse.of(orderTable);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
