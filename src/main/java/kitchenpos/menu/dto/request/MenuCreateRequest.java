package kitchenpos.menu.dto.request;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MenuCreateRequest {

    @NotBlank(message = "이름을 입력해 주세요")
    private final String name;

    @NotNull(message = "금액을 입력해 주세요.")
    @DecimalMin(value = "0", message = "금액이 0원보다 작을 수 없습니다.")
    private final BigDecimal price;

    @NotNull(message = "메뉴그룹 Id를 입력해 주세요.")
    private final Long menuGroupId;

    @NotNull(message = "메뉴에 사용되는 상품을 입력해 주세요.")
    private final List<MenuProductCreateRequest> menuProductRequests;

    public MenuCreateRequest(
            final String name,
            final BigDecimal price,
            final Long menuGroupId,
            final List<MenuProductCreateRequest> menuProductRequests
    ) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
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

    public List<MenuProductCreateRequest> getMenuProductRequests() {
        return menuProductRequests;
    }
}
