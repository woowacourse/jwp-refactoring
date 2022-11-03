package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.product.domain.Price;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UpdatableMenuInfo updatableMenuInfo;
    private Long menuGroupId;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    private Menu(final Long id, final UpdatableMenuInfo updatableMenuInfo, final Long menuGroupId,
                 final List<MenuProduct> menuProducts) {
        this.id = id;
        this.updatableMenuInfo = updatableMenuInfo;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(final String name, final Price price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        this(null, new UpdatableMenuInfo(price, name), menuGroupId, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return updatableMenuInfo.getName();
    }

    public Price getPrice() {
        return updatableMenuInfo.getPrice();
    }

    public UpdatableMenuInfo getUpdatableMenuInfo() {
        return updatableMenuInfo;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }

    public void setName(final String name) {
        updatableMenuInfo = new UpdatableMenuInfo(updatableMenuInfo.getPrice(), name);
    }

    public void setPrice(final Price price) {
        updatableMenuInfo = new UpdatableMenuInfo(price, updatableMenuInfo.getName());
    }
}
