package kitchenpos.application.dtos;

import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GuestNumberRequest {
    @Size(min = 0)
    private int numberOfGuests;

    public GuestNumberRequest(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
