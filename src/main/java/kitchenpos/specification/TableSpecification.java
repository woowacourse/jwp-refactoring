package kitchenpos.specification;

import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.repository.order.TableRepository;
import org.springframework.stereotype.Component;

@Component
public class TableSpecification {

    private final TableRepository tableRepository;

    public TableSpecification(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public void validateChangeNumberOfGuests(OrderTableRequest request) {

        final int numberOfGuests = request.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 수는 음수가 될 수 없습니다.");
        }
    }
}
