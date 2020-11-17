package kitchenpos.dto.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuCreateRequest {
    @NotBlank
    private String name;

    @DecimalMin(value = "0.0", inclusive = false)
    @NotNull
    private BigDecimal price;

    @NotNull
    private Long menuGroupId;

    @NotEmpty
    @Valid
    private List<ProductQuantityRequest> menuProducts;

    public Menu toMenu(MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }
}
