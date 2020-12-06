package kitchenpos.menu.domain;

import kitchenpos.generic.Price;
import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_GROUP_ID", foreignKey = @ForeignKey(name = "FK_MENU_MENU_GROUP"))
    private MenuGroup menuGroup;

    @Embedded
    private final MenuProducts menuProducts = new MenuProducts();

    public Menu() {
    }

    public Menu(String name, Long price, MenuGroup menuGroup) {
        this.name = name;
        this.price = Price.of(price);
        this.menuGroup = menuGroup;
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
        validatePrice();
    }

    private void validatePrice() {
        Price sum = menuProducts.calculateSum();
        if (sum.isLessThan(price)) {
            throw new IllegalArgumentException(String.format("상품 금액의 합(%d)이 메뉴의 가격(%d)보다 작습니다.", sum.longValue(), price.longValue()));
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
