package kitchenpos.order_table.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderTableRequest {

    private Integer numberOfGuests;
    private Boolean empty;
}
