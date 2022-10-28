package kitchenpos.domain.menu;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private MenuPrice menuPrice;

    @Column(name = "menu_group_id", nullable = false)
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    public Menu(final Long id, final String name, final MenuPrice menuPrice, final Long menuGroupId,
                final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.menuPrice = menuPrice;
        this.menuGroupId = menuGroupId;
        this.menuProducts = new MenuProducts(menuProducts);
    }

    public Menu(final String name, final MenuPrice menuPrice, final Long menuGroupId) {
        this(null, name, menuPrice, menuGroupId, new ArrayList<>());
    }

    public void updateProducts(final MenuProducts menuProducts) {
        if (menuPrice.isExpensive(menuProducts.calculateEntirePrice())) {
            throw new IllegalArgumentException("Menu 가격은 Product 가격의 합을 초과할 수 없습니다.");
        }
        this.menuProducts = menuProducts;
    }
}
