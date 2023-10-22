package kitchenpos.domain.table;

import org.springframework.stereotype.Component;

@Component
public class TableValidator {

    public void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("Number of guests must be greater than 0");
        }
    }

    public void validateTableIsEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("Cannot change number of guests of empty table");
        }
    }
}
