package kitchenpos.order.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.value.Price;

@Entity
public class OrderMenu {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @JoinColumn(name = "menu_group_id")
    private Long menuGroupId;

    @OneToMany(fetch = LAZY, mappedBy = "menuId")
    private List<MenuProduct> menuProducts;

    public OrderMenu() {
    }

    public OrderMenu(String name, Price price, Long menuGroupId) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public static OrderMenu from(final Menu menu) {
        return new OrderMenu(
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId()
        );
    }

    public Long getId() {
        return id;
    }
}
