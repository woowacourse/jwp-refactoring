package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import kitchenpos.exception.InvalidMenuException;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this(null, name, price, menuGroup);
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        validateName(this.name);
        validatePrice(this.price);
        validateMenuGroupNull(this.menuGroup);
    }

    private void validateName(String name) {
        validateNull(name);
        validateBlank(name);
    }

    private void validatePrice(BigDecimal price) {
        validateNull(price);
        validateNegative(price);
    }

    private void validateMenuGroupNull(MenuGroup menuGroup) {
        validateNull(menuGroup);
    }

    private void validateNull(Object object) {
        if (Objects.isNull(object)) {
            throw new InvalidMenuException("Menu 정보에 null이 포함되었습니다.");
        }
    }

    private void validateBlank(String name) {
        if (name.replaceAll(" ", "").isEmpty()) {
            throw new InvalidMenuException("Menu name은 공백일 수 없습니다.");
        }
    }

    private void validateNegative(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMenuException(String.format("Menu price는 음수일 수 없습니다. (%s)", price));
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
