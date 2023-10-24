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
    
    private String name;
    
    @Embedded
    private MenuPrice price;
    
    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
    
    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MenuProduct> menuProducts;
    
    public static Menu of(final String name,
                          final BigDecimal menuPrice,
                          final MenuGroup menuGroup,
                          final List<MenuProduct> menuProducts) {
        return new Menu(name,
                new MenuPrice(menuPrice),
                menuGroup,
                menuProducts
        );
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
        putMenuInMenuProducts();
    }
    
    private static void validate(final MenuPrice menuPrice,
                                 final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        menuProducts.stream()
                    .map(MenuProduct::getProductPrice)
                    .map(ProductPrice::getPrice)
                    .forEach(sum::add);
        if (menuPrice.getPrice().compareTo(sum) > 0) {
            throw new InvalidMenuPriceException("메뉴의 가격은 메뉴 상품 가격의 합보다 클 수 없습니다");
        }
    }
    
    private void putMenuInMenuProducts() {
        this.menuProducts.forEach(menuProduct -> new MenuProduct(this,
                menuProduct.getProduct(),
                menuProduct.getQuantity()));
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
