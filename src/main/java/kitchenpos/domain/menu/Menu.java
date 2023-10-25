package kitchenpos.domain.menu;

import static javax.persistence.FetchType.LAZY;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.vo.MenuName;
import kitchenpos.domain.vo.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private MenuName name;
    @Embedded
    private Price price;
    @ManyToOne(optional = false, fetch = LAZY)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_to_menu_group"))
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(final String name,
                final BigDecimal price,
                final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts) {
        this.name = MenuName.from(name);
        this.price = Price.from(price);
        this.menuGroup = menuGroup;
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(this));
        this.menuProducts = new MenuProducts(menuProducts);
        validateTotalPrice();
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return new ArrayList<>(menuProducts.getMenuProducts());
    }
}
