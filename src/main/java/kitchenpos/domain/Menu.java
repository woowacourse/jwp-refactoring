package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.domain.vo.Price;

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

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    private Menu(
            String name,
            Price price,
            Long menuGroupId
    ) {
        this(null, name, price, menuGroupId);
    }

    private Menu(
            Long id,
            String name,
            Price price,
            Long menuGroupId
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public static Menu of(
            String name,
            Long price,
            Long menuGroupId
    ) {
        return new Menu(
                name,
                Price.from(price),
                menuGroupId
        );
    }

    private void addMenuProduct(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
        menuProduct.registerMenu(this);
    }

    public void addAllMenuProducts(List<MenuProduct> menuProducts) {
        menuProducts.forEach(this::addMenuProduct);
        validateTotalPrice();
    }

    private void validateTotalPrice() {
        Price totalPrice = menuProducts
                .stream()
                .map(MenuProduct::totalPrice)
                .reduce(Price.ZERO, Price::add);

        if (price.moreExpensiveThan(totalPrice)) {
            throw new IllegalArgumentException("메뉴의 가격은 포함된 상품들의 합보다 비쌀 수 없습니다.");
        }
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
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

}
