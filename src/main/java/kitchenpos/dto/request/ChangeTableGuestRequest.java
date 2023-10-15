package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ChangeTableGuestRequest {

    private Integer numberOfGuests;

    @JsonCreator
    public ChangeTableGuestRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
