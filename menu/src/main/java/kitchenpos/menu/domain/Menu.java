package kitchenpos.menu.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.product.domain.MenuValidator;
import kitchenpos.product.domain.Product;
import kitchenpos.vo.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    private Long menuGroupId;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(
        name = "menu_id",
        updatable = false, nullable = false
    )
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(final String name, final Price price, final Long menuGroupId) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
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

    public void addProducts(
        final List<Product> products,
        final Map<Long, Long> productIdQuantityMap
    ) {
        products.stream()
            .map(product ->
                new MenuProduct(product.getId(), productIdQuantityMap.get(product.getId()))
            )
            .forEach(menuProducts::add);
    }

    public void validateCreate(final MenuValidator menuValidator) {
        menuValidator.validateMenuCreate(this);
    }
}
