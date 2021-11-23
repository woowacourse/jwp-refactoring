package kitchenpos.menu.domain;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.common.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long menuGroupId;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    @Embedded
    private Price price;

    private String name;

    public Menu(Long id, String name, Price price, Long menuGroupId,
        List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;

        validatePrice(menuProducts);
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.belongsTo(this);
        }
    }

    public Menu(String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu() {
    }

    private void validatePrice(List<MenuProduct> menuProducts) {
        Price sum = menuProducts.stream()
            .map(menuProduct -> menuProduct.getProduct().getPrice())
            .reduce(Price::sum)
            .orElseGet(() -> Price.ZERO);

        if (price.isGreater(sum)) {
            throw new IllegalStateException("메뉴의 가격은 상품들의 가격 합보다 클 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
