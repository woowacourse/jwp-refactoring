package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    // TODO: 20. 11. 17. protected 바꾸기
    public Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        validatePrice(price);
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = getMenuProducts(menuProducts);
    }

    private List<MenuProduct> getMenuProducts(List<MenuProduct> menuProducts) {
        List<MenuProduct> newMenuProducts = new ArrayList<>(menuProducts);
        for (MenuProduct menuProduct : newMenuProducts) {
            menuProduct.setMenu(this);
        }
        return newMenuProducts;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
