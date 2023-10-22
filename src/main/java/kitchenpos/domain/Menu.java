package kitchenpos.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Menu {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    
    private String menuName;
    
    @Embedded
    private Price price;
    
    @ManyToOne
    private MenuGroup menuGroup;
    
    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;
    
    public Menu(final String menuName,
                final Price price,
                final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts) {
        this(null, menuName, price, menuGroup, menuProducts);
    }
    
    public Menu(final Long id,
                final String menuName,
                final Price price,
                final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts) {
        this.id = id;
        this.menuName = menuName;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getMenuName() {
        return menuName;
    }
    
    public Price getPrice() {
        return price;
    }
    
    public MenuGroup getMenuGroup() {
        return menuGroup;
    }
    
    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
