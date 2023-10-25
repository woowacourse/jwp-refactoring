package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Embeddable
public enum OrderStatus {
    COOKING, MEAL, COMPLETION;

    public static List<OrderStatus> notCompleteStatuses() {
        return Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
    }

    public static Optional<OrderStatus> find(final String orderStatusName) {
        return Arrays.stream(OrderStatus.values())
                     .filter(orderStatus -> orderStatus.toString().equals(orderStatusName))
                     .findFirst();
    }

    public String getName() {
        return this.toString();
    }

    public boolean isComplete() {
        return this.equals(COMPLETION);
    }

    public static Optional<OrderStatus> find(final String orderStatusName) {
        return Arrays.stream(OrderStatus.values())
                     .filter(orderStatus -> orderStatus.toString().equals(orderStatusName))
                     .findFirst();
    }

    public String getName() {
        return this.toString();
    }

    public boolean isComplete() {
        return this.equals(COMPLETION);
    }
}
