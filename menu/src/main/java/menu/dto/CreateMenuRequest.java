package menu.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CreateMenuRequest {
    @NotNull
    private final String name;
    @NotNull
    private final long price;
    @NotNull
    private final long menuGroupId;

    private final List<MenuProductRequest> menuProducts;

    private CreateMenuRequest(final String name, final long price, final long menuGroupId, final List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static CreateMenuRequest of(final String name, final long price, final long menuGroupId, final List<MenuProductRequest> menuProducts) {
        return new CreateMenuRequest(name, price, menuGroupId, menuProducts);
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
