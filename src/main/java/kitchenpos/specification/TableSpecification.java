package kitchenpos.specification;

import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.repository.order.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class TableSpecification {

    private final OrderTableRepository orderTableRepository;

    public TableSpecification(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateChangeNumberOfGuests(OrderTableRequest request) {

        final int numberOfGuests = request.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 수는 음수가 될 수 없습니다.");
        }
    }
}
