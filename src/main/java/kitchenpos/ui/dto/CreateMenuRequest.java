package kitchenpos.ui.dto;

import java.math.BigDecimal;
import java.util.List;

public class CreateMenuRequest {

    private Long id;

    private Long menuGroupId;

    private List<MenuProductDto> menuProductDtos;

    private String name;

    private BigDecimal price;

    public CreateMenuRequest(final Long menuGroupId, final List<MenuProductDto> menuProductDtos, final String name, final BigDecimal price) {
        this.menuGroupId = menuGroupId;
        this.menuProductDtos = menuProductDtos;
        this.name = name;
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductDto> getMenuProductDtos() {
        return menuProductDtos;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
