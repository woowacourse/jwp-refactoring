package kitchenpos.application.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GuestNumberRequest {
    private int numberOfGuests;

    public GuestNumberRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
