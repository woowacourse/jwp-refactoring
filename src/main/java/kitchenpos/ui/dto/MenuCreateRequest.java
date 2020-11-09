package kitchenpos.ui.dto;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Price;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(onConstructor_ = @ConstructorProperties({"name", "price", "menuGroupId", "menuProducts"}))
@Getter
public class MenuCreateRequest {
    @NotBlank
    private final String name;

    @NotNull
    @Valid
    private final Price price;

    @NotNull
    private final Long menuGroupId;

    @NotNull
    @Valid
    private final List<MenuProductRequest> menuProducts;

    public Menu toRequestEntity() {
        return Menu.builder()
            .name(name)
            .price(price)
            .menuGroupId(menuGroupId)
            .menuProducts(menuProducts.stream()
                .map(MenuProductRequest::toRequestEntity)
                .collect(Collectors.toList()))
            .build();
    }
}
