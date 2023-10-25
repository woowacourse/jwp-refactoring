package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.dto.vo.MenuName;
import kitchenpos.dto.vo.Price;

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

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    private Menu(String name, Price price, Long menuGroupId) {
        this.name = new MenuName(name);
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        validateMenuProducts(menuProducts);
        this.menuProducts = menuProducts;
    }

    private void validateMenuProducts(List<MenuProduct> menuProducts) {
        long menuProductPriceSum = menuProducts.stream()
                .map(MenuProduct::calculatePrice)
                .mapToLong(price -> price.getValue().longValue())
                .sum();

        if (this.price.isBiggerThan(menuProductPriceSum)) {
            throw new IllegalArgumentException("메뉴 금액이 상품 금액 합계보다 클 수 없습니다.");
        }
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

        private String name;
        private Price price;
        private Long menuGroupId;
        private List<MenuProduct> menuProducts;

        public MenuBuilder name(String name) {
            this.name = name;
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

        public Menu build() {
            return new Menu(name, price, menuGroupId);
        }
    }
}
