package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotNull;
import kitchenpos.menu.exception.InvalidMenuException;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MenuName name;

    @Embedded
    private MenuPrice price;

    @Embedded
    private MenuProducts menuProducts;

    @NotNull
    @JoinColumn(name = "menu_group_id")
    private Long menuGroupId;

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        this(null, new MenuName(name), new MenuPrice(price), menuGroupId);
    }

    public Menu(Long id, MenuName name, MenuPrice price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        validateNull(this.menuGroupId);
    }

    private void validateNull(Long menuGroupId) {
        if (Objects.isNull(menuGroupId)) {
            throw new InvalidMenuException("Menu의 Group은 Null 일 수 없습니다.");
        }
    }

    public void addMenuProducts(MenuProducts menuProducts) {
        this.menuProducts = menuProducts;
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
        return menuProducts.toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
