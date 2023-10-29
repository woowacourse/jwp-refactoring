package kitchenpos.domain;

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
import kitchenpos.application.MenuProductMapper;
import kitchenpos.application.dto.MenuProductQuantityDto;
import kitchenpos.domain.vo.MenuName;
import kitchenpos.domain.vo.MenuPrice;
import kitchenpos.domain.vo.MenuProducts;

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
            List<MenuProductQuantityDto> menuProductQuantities,
            MenuProductMapper menuProductMapper
    ) {
        this.menuName = new MenuName(name);
        this.menuPrice = new MenuPrice(price);
        this.menuGroup = menuGroup;
        this.menuProducts = menuProductMapper.toMenuProducts(menuProductQuantities);
        validateMenuProducts(menuProducts, menuPrice);
    }

    private void validateMenuProducts(MenuProducts menuProducts, MenuPrice menuPrice) {
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
