package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.menu.MenuProductRequest;
import kitchenpos.application.menu.MenuRequest;

public class MenuRequestBuilder {

    private String name = "강정치킨";
    private BigDecimal price = BigDecimal.valueOf(14_000L);
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts = new ArrayList<>();

    public MenuRequestBuilder() {
    }

    public static MenuRequestBuilder aMenuRequest(Long menuGroupId) {
        return new MenuRequestBuilder()
                .withMenuGroupId(menuGroupId);
    }

    public MenuRequestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MenuRequestBuilder withPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public MenuRequestBuilder withMenuProducts(List<MenuProductRequest> menuProducts) {
        this.menuProducts = menuProducts;
        return this;
    }

    public MenuRequest build() {
        return new MenuRequest(name, price, menuGroupId, menuProducts);
    }

    private MenuRequestBuilder withMenuGroupId(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
        return this;
    }
}
