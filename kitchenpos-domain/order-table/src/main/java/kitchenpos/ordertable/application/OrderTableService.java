package kitchenpos.ordertable.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.common.domain.ValidResult;
import kitchenpos.common.exception.KitchenPosException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableChangeEmptyValidator;
import kitchenpos.ordertable.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderTableService {

    private final OrderTableRepository orderTableRepository;
    private final List<OrderTableChangeEmptyValidator> validators;

    public OrderTableService(
        OrderTableRepository orderTableRepository,
        List<OrderTableChangeEmptyValidator> validators
    ) {
        this.orderTableRepository = orderTableRepository;
        this.validators = validators;
    }

    public OrderTableResponse create(OrderTableCreateRequest request) {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(
            null,
            request.isEmpty(),
            request.getNumberOfGuests()
        ));
        return OrderTableResponse.from(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> findAll() {
        return orderTableRepository.findAll().stream()
            .map(OrderTableResponse::from)
            .collect(toList());
    }

    public OrderTableResponse changeEmpty(Long orderTableId, boolean empty) {
        OrderTable orderTable = findOrderTable(orderTableId);
        validators.forEach(validator -> {
            ValidResult result = validator.validate(orderTableId);
            result.throwIfFailure(KitchenPosException::new);
        });
        orderTable.changeEmpty(empty);
        return OrderTableResponse.from(orderTable);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new KitchenPosException("해당 주문 테이블이 없습니다. orderTableId=" + orderTableId));
    }

    public OrderTableResponse changeNumberOfGuests(Long orderTableId, int numberOfGuests) {
        OrderTable orderTable = findOrderTable(orderTableId);
        orderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.from(orderTable);
    }
}
