package kitchenpos.domain.table;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.exception.OrderTableException.NotEnoughGuestsException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static OrderTable of(final int numberOfGuests) {
        return new OrderTable(numberOfGuests);
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final OrderStatusChecker orderStatusChecker, final boolean empty) {
        orderStatusChecker.validateOrderStatusChangeable(List.of(id));
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new NotEnoughGuestsException();
        }

        this.numberOfGuests = numberOfGuests;
    }
}
