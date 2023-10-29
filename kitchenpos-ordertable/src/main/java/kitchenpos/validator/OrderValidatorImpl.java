package kitchenpos.validator;

import kitchenpos.domain.OrderTable;
import kitchenpos.exception.EmptyTableException;
import kitchenpos.exception.NoSuchDataException;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidatorImpl implements OrderValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderValidatorImpl(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validate(Long orderId) {
        final OrderTable orderTable = orderTableRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchDataException("입력한 id의 테이블이 존재하지 않습니다."));

        if (orderTable.isEmpty()) {
            throw new EmptyTableException("비어있는 테이블의 주문은 생성할 수 없습니다.");
        }
    }
}
