package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private Price price;

    @Column(name = "menu_group_id", nullable = false)
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(final Long id,
                final String name,
                final Price price,
                final Long menuGroupId,
                final MenuProducts menuProducts) {
        validateOverPrice(price, menuProducts.getTotalPrice());
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts.join(this);
    }

    public Menu(final String name,
                final Integer price,
                final Long menuGroupId,
                final MenuProducts menuProducts) {
        this(null, name, Price.from(price), menuGroupId, menuProducts);
    }

    private void validateOverPrice(final Price price, final BigDecimal productSumPrice) {
        if (price.isBigger(productSumPrice)) {
            throw new IllegalArgumentException("메뉴 금액은 상품들의 금액 합보다 클 수 없습니다.");
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
        return menuProducts.getProducts();
    }
}
