package kitchenpos.menu.domain;

import static kitchenpos.vo.Price.ZERO_PRICE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.vo.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @Column(nullable = false)
    private Long menuGroupId;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false)
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    private Menu(final Long id,
                 final String name,
                 final Price price,
                 final Long menuGroupId,
                 final List<MenuProduct> menuProducts) {
        validateMenuPrice(price, calculateSumByMenuProducts(menuProducts));

        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu of(final String name,
                          final BigDecimal price,
                          final Long menuGroupId,
                          final List<MenuProduct> menuProducts) {
        return new Menu(null, name, new Price(price), menuGroupId, menuProducts);
    }

    public static Menu of(final String name,
                          final BigDecimal price,
                          final Long menuGroupId) {
        return new Menu(null, name, new Price(price), menuGroupId, new ArrayList<>());
    }

    private Price calculateSumByMenuProducts(
            final List<MenuProduct> menuProducts
    ) {
        return menuProducts.stream()
                .map(MenuProduct::caculateTotalPrice)
                .reduce(ZERO_PRICE, Price::add);
    }

    private void validateMenuPrice(
            final Price price,
            final Price calculatedPrice
    ) {
        if (price.isGreaterThan(calculatedPrice)) {
            throw new IllegalArgumentException("메뉴 가격은 메뉴 상품 가격의 합보다 클 수 없습니다.");
        }
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
