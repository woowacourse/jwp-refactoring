package kitchenpos.application.dtos;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import kitchenpos.domain.Menu;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MenuRequest {
    private String name;
    @NotNull
    @Size
    private Long price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest(String name, Long price, Long menuGroupId,
                       List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public MenuRequest(Menu menu){
        this.name = menu.getName();
        this.price = menu.getPrice().longValue();
        this.menuGroupId = menu.getMenuGroupId();
        this.menuProducts = menu.getMenuProducts().stream()
                .map(MenuProductRequest::new)
                .collect(Collectors.toList());
    }
}
