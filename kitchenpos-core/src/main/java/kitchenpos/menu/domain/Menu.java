package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.product.domain.Price;

@Entity
public class Menu {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;

    @Embedded
    private Price price;

    private Long menuGroupId;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(final String name,
                final Price price,
                final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(final Long id,
                final String name,
                final Price price,
                final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public void addMenuProducts(final List<MenuProduct> menuProducts) {
        validateMenuProducts(menuProducts);
        this.menuProducts = menuProducts;
    }

    private void validateMenuProducts(final List<MenuProduct> menuProducts) {
        final BigDecimal sum = menuProducts.stream()
                                           .map(MenuProduct::calculatePrice)
                                           .reduce(BigDecimal::add)
                                           .orElseThrow(() -> new IllegalArgumentException("메뉴 상품의 가격 계산 중 오류가 발생했습니다."));
        if (price.getValue().compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 상품들의 총 가격보다 클 수 없습니다.");
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", menuGroupId=" + menuGroupId +
                ", menuProducts=" + menuProducts +
                '}';
    }
}
