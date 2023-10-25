package kitchenpos.domain.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.domain.Money;
import kitchenpos.domain.product.Product;
import org.springframework.util.CollectionUtils;

import static java.util.Objects.requireNonNull;
import static javax.persistence.CascadeType.ALL;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price"))
    private Money price;

    private Long menuGroupId;

    @OneToMany(cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public static Menu of(final String name, final Money price, final Long menuGroupId,
                          final Map<Product, Integer> productToQuantity) {
        Menu menu = new Menu(null, name, price, menuGroupId);
        menu.changeMenuProducts(productToQuantity);
        return menu;
    }

    private Menu(final Long id, final String name, final Money price, final Long menuGroupId) {
        this.id = id;
        this.name = requireNonNull(name, "메뉴 이름은 null일 수 없습니다.");
        this.price = requireNonNull(price, "메뉴 가격은 null일 수 없습니다.");
        this.menuGroupId = menuGroupId;
    }

    public Menu(final Long id, final String name, final Money price, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = requireNonNull(name, "메뉴 이름은 null일 수 없습니다.");
        this.price = requireNonNull(price, "메뉴 가격은 null일 수 없습니다.");
        this.menuGroupId = menuGroupId;
        if (CollectionUtils.isEmpty(menuProducts)) {
            throw new IllegalArgumentException("메뉴에 속한 상품은 null이나 비어있을 수 없습니다.");
        }
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void changeMenuProducts(final Map<Product, Integer> productToQuantity) {
        if (CollectionUtils.isEmpty(productToQuantity)) {
            throw new IllegalArgumentException("메뉴에 속한 상품은 null이나 비어있을 수 없습니다.");
        }
        validatePrice(Money.ZERO, productToQuantity);
        final List<MenuProduct> menuProducts = productToQuantity.keySet().stream()
                .map(product -> new MenuProduct(null, product.getId(), product.getName(), product.getPrice(),
                        productToQuantity.get(product)))
                .collect(Collectors.toList());
        this.menuProducts = menuProducts;
    }

    private void validatePrice(final Money initialPrice, final Map<Product, Integer> productToQuantity) {
        final var sumOfMenuProductPrice = productToQuantity.entrySet().stream()
                .map(entry -> entry.getKey().getPrice().times(entry.getValue()))
                .reduce(initialPrice, Money::plus);
        if (price.isHigherThan(sumOfMenuProductPrice)) {
            throw new IllegalArgumentException("메뉴 가격이 메뉴에 속한 상품 가격의 합보다 큽니다.");
        }
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

}
