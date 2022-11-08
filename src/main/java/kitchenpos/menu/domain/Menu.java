package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.Hibernate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import kitchenpos.menu.dto.application.MenuProductDto;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "menu_group_id", nullable = false)
    private Long menuGroupId;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "menu")
    @JsonIgnore
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(
        final String name,
        final BigDecimal price,
        final Long menuGroupId,
        final List<MenuProductDto> menuProducts,
        final MenuValidator validator) {
        validator.validateCreateMenu(name, price, menuGroupId,menuProducts);
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        for (MenuProductDto product : menuProducts) {
            MenuProduct menuProduct = new MenuProduct(this, product.getProductId(), product.getQuantity());
            this.menuProducts.add(menuProduct);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Menu menu = (Menu)o;
        return id != null && Objects.equals(id, menu.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
