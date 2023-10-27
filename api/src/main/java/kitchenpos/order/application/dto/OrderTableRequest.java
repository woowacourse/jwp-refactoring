package kitchenpos.order.application.dto;

import java.util.Objects;
import kitchenpos.order.domain.OrderTable;

public class OrderTableRequest {

    private int numberOfGuest;

    private OrderTableRequest() {
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuest);
    }

    public int getNumberOfGuest() {
        return numberOfGuest;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTableRequest that = (OrderTableRequest) o;
        return numberOfGuest == that.numberOfGuest;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuest);
    }
}
