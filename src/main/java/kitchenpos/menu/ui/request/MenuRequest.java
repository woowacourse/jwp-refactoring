package kitchenpos.menu.ui.request;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MenuRequest {

    @NotBlank(message = "메뉴 이름이 null이거나 비어있습니다.")
    private String name;

    @NotNull(message = "메뉴의 가격이 null입니다.")
    @Min(value = 0, message = "메뉴의 가격이 0보다 작습니다.")
    private BigDecimal price;

    @NotNull(message = "메뉴 그룹의 아이디가 null입니다.")
    private Long menuGroup;

    @NotEmpty(message = "메뉴 상품이 비어있습니다.")
    private List<MenuProductRequest> menuProducts;

    protected MenuRequest() {
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroup, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
