package kitchenpos.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItemRequest {
    @NotNull
    private Long menuId;

    @Min(0)
    @NotNull
    private Long quantity;
}
