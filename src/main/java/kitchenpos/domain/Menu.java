package kitchenpos.domain;

import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    public Menu() {
    }

    public Menu(MenuRequest menuRequest, MenuGroup menuGroup) {
        this(null, menuRequest.getName(), menuRequest.getPrice(), menuGroup);
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        validateName(name);
        validatePrice(price);
        validateMenuGroup(menuGroup);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public void validateTotalPrice(BigDecimal totalPrice) {
        if (this.price.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException("메뉴가격은 상품가격보다 클 수 없습니다.");
        }
    }

    private void validateMenuGroup(MenuGroup menuGroup) {
        if (Objects.isNull(menuGroup)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }
    }

    private void validateName(String name) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 메뉴 이름입니다.");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("유효하지 않은 가격입니다.");
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }
}
