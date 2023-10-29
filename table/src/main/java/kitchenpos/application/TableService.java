package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.ChangeEmptyValidator;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final ChangeEmptyValidator changeEmptyValidator;
    private final OrderTableRepository orderTableRepository;

    public TableService(final ChangeEmptyValidator changeEmptyValidator,
                        final OrderTableRepository orderTableRepository) {
        this.changeEmptyValidator = changeEmptyValidator;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableDto create(final CreateTableCommand command) {
        OrderTable table = orderTableRepository.save(command.toDomain());
        return OrderTableDto.from(table);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTableDto changeEmpty(final ChangeTableEmptyCommand request) {
        Long tableId = request.getTableId();
        final OrderTable orderTable = orderTableRepository.getById(tableId);

        orderTable.changeEmpty(changeEmptyValidator);
        return OrderTableDto.from(orderTable);
    }

    @Transactional
    public OrderTableDto changeNumberOfGuests(final ChangeNumberOfGuestsCommand request) {
        final OrderTable foundTable = orderTableRepository.getById(request.getOrderTableId());

        foundTable.changeNumberOfGuests(request.getNumberOfGuests());
        OrderTable orderTable = orderTableRepository.save(foundTable);
        return OrderTableDto.from(orderTable);
    }

}
