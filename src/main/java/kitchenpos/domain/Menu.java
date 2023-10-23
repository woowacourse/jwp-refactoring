package kitchenpos.domain;

import kitchenpos.domain.exception.InvalidMenuPriceException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    
    private String menuName;
    
    @Embedded
    private MenuPrice menuPrice;
    
    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
    
    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;
    
    public Menu(final String menuName,
                final MenuPrice menuPrice,
                final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts) {
        this(null, menuName, menuPrice, menuGroup, menuProducts);
    }
    
    public Menu(final Long id,
                final String menuName,
                final MenuPrice menuPrice,
                final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts) {
        validate(menuPrice, menuProducts);
        this.id = id;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }
    
    public static Menu of(final String name,
                          final BigDecimal menuPrice,
                          final MenuGroup menuGroup,
                          final List<MenuProduct> menuProducts) {
        return new Menu(name,
                new MenuPrice(menuPrice),
                menuGroup,
                menuProducts);
    }
    
    private static void validate(final MenuPrice menuPrice,
                                 final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        menuProducts.stream()
                    .map(MenuProduct::getProductPrice)
                    .map(ProductPrice::getProductPrice)
                    .forEach(sum::add);
        if (menuPrice.getPrice().compareTo(sum) > 0) {
            throw new InvalidMenuPriceException("메뉴의 가격은 메뉴 상품 가격의 합보다 클 수 없습니다");
        }
    }
    
    public Long getId() {
        return id;
    }
    
    public String getMenuName() {
        return menuName;
    }
    
    public MenuPrice getPrice() {
        return menuPrice;
    }
    
    public MenuGroup getMenuGroup() {
        return menuGroup;
    }
    
    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
