package kitchenpos.core.menu.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import kitchenpos.core.product.domain.Price;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.MappedCollection;

public class Menu {
    @Id
    private Long id;
    private String name;
    @Embedded.Empty
    private Price price;
    private Long menuGroupId;
    @MappedCollection(idColumn = "MENU_ID")
    private Set<MenuProduct> menuProducts;

    @PersistenceCreator
    public Menu() {
    }

    public Menu(final Long id, final String name, final Price price, final Long menuGroupId,
                final MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = new HashSet<>(menuProducts.getMenuProducts());
    }

    public Menu(final String name, final Price price, final Long menuGroupId, final MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = new HashSet<>(menuProducts.getMenuProducts());
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
        return new ArrayList<>(menuProducts);
    }
}
