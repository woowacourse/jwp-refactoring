package kitchenpos.ui.dto;

import java.util.Objects;
import javax.validation.constraints.NotNull;

public class TableChangeNumberOfGuestsRequest {

    @NotNull
    private Integer numberOfGuests;

    private TableChangeNumberOfGuestsRequest() {
    }

    public TableChangeNumberOfGuestsRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableChangeNumberOfGuestsRequest that = (TableChangeNumberOfGuestsRequest) o;
        return Objects.equals(numberOfGuests, that.numberOfGuests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }

    @Override
    public String toString() {
        return "TableChangeNumberOfGuestsRequest{" +
            "numberOfGuests=" + numberOfGuests +
            '}';
    }
}
