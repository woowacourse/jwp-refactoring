package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import kitchenpos.menugroup.domain.MenuGroup;
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

    @NotNull
    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this(null, new MenuName(name), new MenuPrice(price), menuGroup);
    }

    public Menu(Long id, MenuName name, MenuPrice price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        validateMenuGroupNull(this.menuGroup);
    }

    private void validateMenuGroupNull(MenuGroup menuGroup) {
        if (Objects.isNull(menuGroup)) {
            throw new InvalidMenuException("Menu 정보에 null이 포함되었습니다.");
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
        return menuGroup.getId();
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
