package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.command.ChangeNumberOfOrderTableGuestsCommand;
import kitchenpos.application.command.ChangeOrderTableEmptyCommand;
import kitchenpos.application.command.CreateOrderTableCommand;
import kitchenpos.application.response.OrderTableResponse;
import kitchenpos.application.verifier.ChangeOrderTableEmptyVerifier;
import kitchenpos.domain.model.ordertable.OrderTable;
import kitchenpos.domain.model.ordertable.OrderTableRepository;

@Service
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;
    private final ChangeOrderTableEmptyVerifier changeOrderTableEmptyVerifier;

    public OrderTableService(final OrderTableRepository orderTableRepository,
            ChangeOrderTableEmptyVerifier changeOrderTableEmptyVerifier) {
        this.orderTableRepository = orderTableRepository;
        this.changeOrderTableEmptyVerifier = changeOrderTableEmptyVerifier;
    }

    @Transactional
    public OrderTableResponse create(final CreateOrderTableCommand command) {
        OrderTable orderTable = command.toEntity();
        OrderTable saved = orderTableRepository.save(orderTable);
        return OrderTableResponse.of(saved);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.listOf(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId,
            final ChangeOrderTableEmptyCommand command) {
        OrderTable orderTable = changeOrderTableEmptyVerifier.toOrderTable(orderTableId);
        orderTable.changeEmpty(command.isEmpty());
        OrderTable changed = orderTableRepository.save(orderTable);
        return OrderTableResponse.of(changed);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
            final ChangeNumberOfOrderTableGuestsCommand command) {
        final OrderTable saved = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        saved.changeNumberOfGuests(command.getNumberOfGuests());
        OrderTable changed = orderTableRepository.save(saved);
        return OrderTableResponse.of(changed);
    }
}
