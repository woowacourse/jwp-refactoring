package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.menu.domain.dto.vo.MenuName;
import kitchenpos.menu.domain.dto.vo.Price;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Embedded
    private MenuName name;

    @Embedded
    private Price price;

    @Column(name = "menu_group_id")
    private Long menuGroupId;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "menu_id", updatable = false, nullable = false)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    private Menu(MenuName name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public boolean hasBiggerPriceThan(long price) {
        return this.price.isBiggerThan(price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public static MenuBuilder builder() {
        return new MenuBuilder();
    }

    public static class MenuBuilder {

        private MenuName name;
        private Price price;
        private Long menuGroupId;
        private List<MenuProduct> menuProducts;

        public MenuBuilder name(String name) {
            this.name = new MenuName(name);
            return this;
        }

        public MenuBuilder price(long price) {
            this.price = Price.of(price);
            return this;
        }

        public MenuBuilder menuGroupId(Long menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }

        public MenuBuilder menuProducts(List<MenuProduct> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            return new Menu(name, price, menuGroupId, menuProducts);
        }
    }
}
