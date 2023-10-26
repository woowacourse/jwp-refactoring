package kitchenpos.table.domain;

import org.springframework.stereotype.Component;

@Component
public class TableChangeNumberOfGuestValidator {

    public void validate(final int numberOfGuest, final OrderTable orderTable) {
        validateNumberOfGuests(numberOfGuest);
        validateTableIsEmpty(orderTable);
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("Number of guests must be greater than 0");
        }
    }

    private void validateTableIsEmpty(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("Cannot change number of guests of empty table");
        }
    }
}
