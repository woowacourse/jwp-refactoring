package kitchenpos.dto.menu;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CreateMenuRequest {
    //"name": "후라이드+후라이드",
    //  "price": 19000,
    //  "menuGroupId": 1,
    //  "menuProducts": [
    //    {
    //      "productId": 1,
    //      "quantity": 2
    //    }
    //  ]
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

    public long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
