package kitchenpos.core.domain.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import kitchenpos.core.domain.product.Product;
import kitchenpos.core.domain.vo.MenuName;
import kitchenpos.core.domain.vo.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private MenuName name;
    @Embedded
    private Price price;
    @JoinColumn(table = "menu_group", name = "menu_group_id",
            foreignKey = @ForeignKey(name = "fk_menu_to_menu_group"),
            nullable = false)
    private long menuGroupId;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(final Long id,
                final String name,
                final BigDecimal price,
                final long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = MenuName.from(name);
        this.price = Price.from(price);
        this.menuGroupId = menuGroupId;
        menuProducts.forEach(menuProduct -> menuProduct.register(this));
        this.menuProducts = new MenuProducts(menuProducts);
        validateTotalPrice();
    }

    public Menu(final String name,
                final BigDecimal price,
                final long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    private void validateTotalPrice() {
        if (price.isBiggerThan(menuProducts.totalPrice())) {
            throw new IllegalArgumentException("메뉴 가격은 메뉴에 포함된 상품 가격의 총합보다 클 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.value();
    }

    public long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return new ArrayList<>(menuProducts.getMenuProducts());
    }

    public Map<Product, Long> getQuantityByProduct() {
        return menuProducts.getQuantityByProduct();
    }
}
