package kitchenpos.domain.menu;

import kitchenpos.domain.common.Money;
import kitchenpos.domain.common.Name;

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
    @Embedded
    private Name name;
    @Embedded
    private Money price;
    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(final Long id, final Name name, final Money price, final MenuGroup menuGroup, final MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        validateMenuPrice(price, menuProducts.calculateSum());
        this.menuProducts = menuProducts;
    }

    public static Menu of(final Name name, final Money price, final MenuGroup menuGroup, final MenuProducts menuProducts) {
        return new Menu(null, name, price, menuGroup, menuProducts);
    }

    public static Menu of(final String name, final long price, final MenuGroup menuGroup, final MenuProducts menuProducts) {
        return new Menu(null, Name.of(name), Money.valueOf(price), menuGroup, menuProducts);
    }


    private static void validateMenuPrice(final Money price, final Money sum) {
        if (price.isBiggerThan(sum)) {
            throw new IllegalArgumentException("메뉴의 가격은 상품의 가격보다 클 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

}
