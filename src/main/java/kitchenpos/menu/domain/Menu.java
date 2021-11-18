package kitchenpos.menu.domain;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.name.Name;
import kitchenpos.price.Price;

@Entity
public class Menu {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;
    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup,
                final MenuProducts menuProducts) {
        this(null, new Name(name), new Price(price), menuGroup, menuProducts);
    }

    public Menu(final Long id, final Name name, final Price price, final MenuGroup menuGroup,
                final MenuProducts menuProducts) {
        validateMenuProducts(menuProducts, price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private void validateMenuProducts(final MenuProducts menuProducts, final Price price) {
        Price totalMenuProductsPrice = menuProducts.totalPrice();
        if (price.isBiggerThan(totalMenuProductsPrice)) {
            throw new IllegalArgumentException(String.format(
                "메뉴 가격은 상품 가격의 합보다 클 수 없습니다.(메뉴 가격: %d, 상품 가격: %d",
                price.getValue().intValue(),
                totalMenuProductsPrice.getValue().intValue()
            ));
        }
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
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
