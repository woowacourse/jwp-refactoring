package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MenuName name;

    private MenuPrice price;

    @NotNull
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(
            String name,
            BigDecimal price,
            Long menuGroupId,
            MenuProducts menuProducts,
            MenuValidator menuValidator
    ) {
        validateMenuGroup(menuGroupId);
        menuValidator.validate(menuProducts, price, menuGroupId);

        this.name = MenuName.from(name);
        this.price = MenuPrice.from(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu create(
            String name,
            BigDecimal price,
            Long menuGroupId,
            MenuProducts menuProducts,
            MenuValidator menuValidator
    ) {
        return new Menu(name, price, menuGroupId, menuProducts, menuValidator);
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (menuGroupId == null) {
            throw new NullPointerException("메뉴 그룹은 null일 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

}
