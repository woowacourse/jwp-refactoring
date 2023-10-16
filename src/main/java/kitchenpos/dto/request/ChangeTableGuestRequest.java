package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sun.istack.NotNull;

public class ChangeTableGuestRequest {

    @NotNull
    private Integer numberOfGuests;

    @JsonCreator
    public ChangeTableGuestRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
