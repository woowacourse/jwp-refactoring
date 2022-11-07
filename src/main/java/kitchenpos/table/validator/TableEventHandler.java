package kitchenpos.table.validator;

import org.springframework.stereotype.Component;

import kitchenpos.order.domain.ChangeTableValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@Component
public class TableEventHandler implements ChangeTableValidator {

    private final OrderTableRepository orderTableRepository;

    public TableEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void validateOrderableTable(Long id) {
        OrderTable orderTable = orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블입니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있습니다.");
        }
    }
}
