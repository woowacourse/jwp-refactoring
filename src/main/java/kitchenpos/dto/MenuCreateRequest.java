package kitchenpos.dto;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class MenuCreateRequest {
    @NotBlank(message = "메뉴의 이름은 반드시 존재해야 합니다!")
    private String name;

    @NotNull(message = "메뉴의 가격은 반드시 존재해야 합니다!")
    private BigDecimal price;

    @NotNull(message = "메뉴가 속한 그룹은 반드시 존재해야 합니다!")
    private Long menuGroupId;

    @NotEmpty
    private List<MenuProductCreateRequest> menuProductCreateRequests;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest(String name, BigDecimal price, Long menuGroupId,
                             List<MenuProductCreateRequest> menuProductCreateRequests) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductCreateRequests = menuProductCreateRequests;
    }

    public Menu toMenu(MenuGroup menuGroup) {
        return new Menu(this.name, this.price, menuGroup);
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

    public List<MenuProductCreateRequest> getMenuProductCreateRequests() {
        return menuProductCreateRequests;
    }
}
