package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * MenuGroup에 속하는 실제 주문 가능 단위
 */
@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "menu_group_id")
    private Long menuGroupId;

    @OneToMany(mappedBy = "menuId")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
        final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = List.copyOf(menuProducts);
        validatePrice(price);
    }

    protected Menu() {
    }

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId) {
        this(id, name, price, menuGroupId, Collections.emptyList());
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void validatePriceUnderProductsSum(final List<Product> products) {
        if (price.compareTo(calculateProductsSum(products)) > 0) {
            throw new IllegalArgumentException("price must be equal to or less than the sum of product prices");
        }
    }

    public void setIdToMenuProducts() {
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(id);
        }
    }

    private BigDecimal calculateProductsSum(final List<Product> products) {
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < menuProducts.size(); i++) {
            validateMenuProductMatchProduct(products.get(i), i);
            sum = sum.add(products.get(i).getPrice().multiply(BigDecimal.valueOf(menuProducts.get(i).getQuantity())));
        }
        return sum;
    }

    private void validateMenuProductMatchProduct(final Product product, final int index) {
        if (!product.isSameId(menuProducts.get(index).getProductId())) {
            throw new IllegalArgumentException("menu product not match product");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return List.copyOf(menuProducts);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Menu menu = (Menu)o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
