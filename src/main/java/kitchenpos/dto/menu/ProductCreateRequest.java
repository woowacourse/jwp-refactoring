package kitchenpos.dto.menu;

import kitchenpos.domain.menu.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {
    @NotBlank
    private String name;

    @DecimalMin(value = "0.0", inclusive = false)
    @NotNull
    private BigDecimal price;

    public Product toProduct() {
        return new Product(name, price);
    }
}
