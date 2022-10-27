package kitchenpos.application.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuCreateRequest {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProductsDto;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest(final Long id, final String name, final BigDecimal price, final Long menuGroupId,
                             final List<MenuProductDto> menuProductsDto) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductsDto = menuProductsDto;
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

    public List<MenuProductDto> getMenuProductsDto() {
        return menuProductsDto;
    }

    public Menu toMenu() {
        return new Menu(
                this.id,
                this.name,
                this.price,
                this.menuGroupId,
                toMenuProducts());
    }

    private List<MenuProduct> toMenuProducts() {
        return this.menuProductsDto.stream()
                .map(MenuProductDto::toMenuProduct)
                .collect(Collectors.toList());
    }
}
