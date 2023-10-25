package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private MenuPrice price;

    @Embedded
    private MenuProducts menuProducts;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    protected Menu() {
    }

    public Menu(final Long id, final String name, final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.menuProducts = new MenuProducts(menuProducts, this);
        this.price = this.menuProducts.calculatePrice();
    }

    public static Menu forSave(final String name, final List<MenuProduct> menuProducts) {
        return new Menu(null, name, menuProducts);
    }

    public void joinMenuGroup(final MenuGroup menuGroup) {
        validateMenuGroup();
        this.menuGroup = menuGroup;
    }

    private void validateMenuGroup() {
        if (this.menuGroup != null) {
            throw new IllegalArgumentException("메뉴 그룹이 이미 존재합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MenuPrice getPrice() {
        return price;
    }

    public List<MenuProduct> getMenuProducts() {
        return new ArrayList<>(menuProducts.getMenuProducts());
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public boolean hasSamePrice(final int price) {
        final MenuPrice givenMenuPrice = new MenuPrice(BigDecimal.valueOf(price));

        return this.price.equals(givenMenuPrice);
    }
}
