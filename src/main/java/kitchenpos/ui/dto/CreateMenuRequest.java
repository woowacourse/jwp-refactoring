package kitchenpos.ui.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateMenuDto;
import kitchenpos.application.dto.CreateMenuProductDto;

public class CreateMenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<CreateMenuProductRequest> menuProducts;

    protected CreateMenuRequest() {
    }

    public CreateMenuRequest(final String name, final BigDecimal price, final Long menuGroupId,
                             final List<CreateMenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public CreateMenuDto toCreateMenuDto() {
        List<CreateMenuProductDto> menuProductsDto = menuProducts.stream()
                .map(CreateMenuProductRequest::toCreateMenuProductDto)
                .collect(Collectors.toList());
        return new CreateMenuDto(name, price, menuGroupId, menuProductsDto);
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

    public List<CreateMenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}
