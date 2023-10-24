package kitchenpos.domain.menu;

import kitchenpos.domain.common.Price;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Menu {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @OneToOne(fetch = LAZY)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(final String name, final Price price, final MenuGroup menuGroup, final MenuProducts menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public Menu(final Long id, final String name, final Price price, final MenuGroup menuGroup, final MenuProducts menuProducts) {
        validateNotNull(price, menuGroup);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private void validateNotNull(final Price price, final MenuGroup menuGroup) {
        if (price == null) {
            throw new IllegalArgumentException("Price의 값은 null 일 수 없습니다.");
        }

        if (menuGroup == null) {
            throw new IllegalArgumentException("MenuGroup의 값은 null 일 수 없습니다.");
        }
    }

    public void addMenuProducts(final MenuProducts toAddMenuProducts) {
        final Price sum = toAddMenuProducts.getPriceSumOfProducts();

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        toAddMenuProducts.getMenuProducts().forEach(menuProduct -> menuProduct.changeMenu(this));
        this.menuProducts.addAll(toAddMenuProducts.getMenuProducts());
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
