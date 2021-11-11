package kitchenpos.domain;

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

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    @Embedded
    private Price price;

    private String name;

    public Menu(Long id, String name, Price price, MenuGroup menuGroup,
        List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;

        validatePrice(menuProducts);
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.belongsTo(this);
        }
    }

    public Menu(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
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
