package core.domain;

import core.vo.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Menu extends AbstractAggregateRoot<Menu> {
    @Id
    private Long id;
    private String name;
    private Money price;
    @Column("MENU_GROUP_ID")
    private AggregateReference<MenuGroup, Long> menuGroup;

    @MappedCollection(idColumn = "MENU_ID")
    private Set<MenuProduct> menuProducts;

    public Menu(String name, BigDecimal price, Long menuGroup) {
        this(name, price, menuGroup, new ArrayList<>());
    }

    public Menu(String name, BigDecimal price, Long menuGroup, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    @PersistenceCreator
    public Menu(Long id, String name, BigDecimal price, Long menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = new Money(price);
        this.menuGroup = AggregateReference.to(menuGroup);
        this.menuProducts = new HashSet<>(menuProducts);
    }

    public static Menu createWithoutId(String name, BigDecimal price, Long menuGroup, List<MenuProduct> menuProducts, MenuValidator menuValidator) {
        Menu menu = new Menu(null, name, price, menuGroup, menuProducts);
        menuValidator.validate(menu);
        return menu;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return new ArrayList<>(menuProducts);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Menu menu = (Menu) object;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
