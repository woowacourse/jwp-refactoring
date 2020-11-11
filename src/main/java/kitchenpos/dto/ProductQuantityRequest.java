package kitchenpos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductQuantityRequest {
    @NotNull
    private Long productId;

    @NotNull
    @Positive
    private Long quantity;
}
