package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.menu.domain.vo.MenuName;
import kitchenpos.menu.domain.vo.MenuPrice;
import kitchenpos.menu.domain.vo.MenuProducts;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MenuName menuName;

    @Embedded
    private MenuPrice menuPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this.menuName = new MenuName(name);
        this.menuPrice = new MenuPrice(price);
        this.menuGroup = menuGroup;
    }

    public Menu(
            String name,
            BigDecimal price,
            MenuGroup menuGroup,
            MenuProducts menuProducts
    ) {
        this.menuName = new MenuName(name);
        this.menuPrice = new MenuPrice(price);
        this.menuGroup = menuGroup;
        validate(menuProducts, menuPrice);
        this.menuProducts = menuProducts;
    }

    private void validate(MenuProducts menuProducts, MenuPrice menuPrice) {
        if (menuProducts.isPriceLessThan(menuPrice.getPrice())) {
            throw new IllegalArgumentException("메뉴 가격이 상품 가격의 합보다 클 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return menuName.getName();
    }

    public BigDecimal getPrice() {
        return menuPrice.getPrice();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }
}
