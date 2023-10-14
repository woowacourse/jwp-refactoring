package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.common.BaseEntity;

@Entity
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProductsItems) {
        MenuProducts menuProducts = new MenuProducts(menuProductsItems);
        validate(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private void validate(BigDecimal price, MenuProducts menuProducts) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴의 가격은 0원 이상이어야 합니다.");
        }
        if (price.compareTo(menuProducts.calculateAmount()) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 상품들의 금액의 합보다 클 수 없습니다.");
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

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
