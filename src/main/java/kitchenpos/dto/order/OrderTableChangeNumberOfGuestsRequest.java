package kitchenpos.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderTableChangeNumberOfGuestsRequest {
    @Min(1)
    @NotNull
    private Integer numberOfGuests;
}
