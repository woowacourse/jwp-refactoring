package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.domain.vo.Price;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    protected Menu() {
    }

    private Menu(String name, Price price, MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public static MenuBuilder builder() {
        return new MenuBuilder();
    }

    public static class MenuBuilder {

        private String name;
        private Price price;
        private MenuGroup menuGroup;
        private List<MenuProduct> menuProducts;

        public MenuBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MenuBuilder price(long price) {
            this.price = Price.of(price);
            return this;
        }

        public MenuBuilder menuGroup(MenuGroup menuGroup) {
            this.menuGroup = menuGroup;
            return this;
        }

        public Menu build() {
            return new Menu(name, price, menuGroup);
        }
    }
}
