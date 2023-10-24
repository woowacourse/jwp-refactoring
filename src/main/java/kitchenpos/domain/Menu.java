package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Embedded
    private Price price;

    @ManyToOne
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(final String name, final Price price, final MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = new ArrayList<>();
    }

    public void addMenuProducts(final List<MenuProduct> menuProducts) {
        validateMenuPrice(menuProducts);
        this.menuProducts.addAll(menuProducts);
    }

    private void validateMenuPrice(final List<MenuProduct> menuProducts) {
        final Price productsPriceSum = calculateTotalPriceOfProducts(menuProducts);
        if (price.isGreaterThan(productsPriceSum)) {
            throw new IllegalArgumentException("메뉴 가격은 메뉴 상품 가격 총합보다 클 수 없습니다.");
        }
    }

    private Price calculateTotalPriceOfProducts(final List<MenuProduct> menuProducts) {
        Price sum = Price.ZERO;

        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = menuProduct.getProduct();
            final Price productPrice = Price.from(product.getPrice());
            sum = sum.add(productPrice.multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        return sum;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public Long getMenuGroupId() {
        if (menuGroup == null) {
            return null;
        }

        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
