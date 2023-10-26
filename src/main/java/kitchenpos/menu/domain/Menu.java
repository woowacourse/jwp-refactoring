package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.menu.exception.InvalidMenuPriceException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    
    private String name;
    
    @Embedded
    private MenuPrice price;
    
    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
    
    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();
    
    public Menu() {
    }
    
    public static Menu of(final String name,
                          final BigDecimal menuPrice,
                          final MenuGroup menuGroup,
                          final List<MenuProduct> menuProducts) {
        Menu menu = new Menu(name,
                new MenuPrice(menuPrice),
                menuGroup,
                menuProducts
        );
        menu.putMenuInMenuProducts();
        return menu;
    }
    
    public Menu(final String name,
                final MenuPrice price,
                final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }
    
    public Menu(final Long id,
                final String name,
                final MenuPrice price,
                final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts) {
        validate(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }
    
    private void validate(final MenuPrice menuPrice,
                                 final List<MenuProduct> menuProducts) {
        BigDecimal sum = menuProducts.stream()
                    .map(MenuProduct::getProductPrice)
                    .map(ProductPrice::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (menuPrice.getPrice().compareTo(sum) > 0) {
            throw new InvalidMenuPriceException("메뉴 가격은 메뉴 상품 가격의 합보다 클 수 없습니다");
        }
    }
    
    public void putMenuInMenuProducts() {
        this.menuProducts.forEach(menuProduct -> menuProduct.setMenu(this));
    }
    
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public MenuPrice getPrice() {
        return price;
    }
    
    public MenuGroup getMenuGroup() {
        return menuGroup;
    }
    
    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
