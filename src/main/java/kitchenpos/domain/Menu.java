package kitchenpos.domain;

import java.util.List;
import kitchenpos.domain.product.Price;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.MappedCollection;

public class Menu {
    @Id
    private Long id;
    private String name;
    @Embedded.Nullable
    private Price price;
    private Long menuGroupId;
    @MappedCollection(idColumn = "MENU_ID", keyColumn = "SEQ")
    private List<MenuProduct> menuProducts;

    public Menu(final Long id, final String name, final Price price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
