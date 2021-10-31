package kitchenpos.application;

import java.util.List;
import kitchenpos.application.dto.TableRequest;
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
    public OrderTable create(final TableRequest tableRequest) {
        OrderTable orderTable = tableMapper.mapFrom(tableRequest);
        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final TableRequest tableRequest) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.updateEmpty(tableValidator, tableRequest.getEmpty());
        return orderTableRepository.save(orderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final TableRequest tableRequest) {
        final OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.updateNumberOfGuests(tableRequest.getNumberOfGuests());
        return orderTableRepository.save(orderTable);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
