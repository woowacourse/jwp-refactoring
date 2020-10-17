package kitchenpos.dto.menu;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class menuCreateRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProductDtos;

    public menuCreateRequest() {
    }

    public menuCreateRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductDto> menuProductDtos) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductDtos = menuProductDtos;
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

    public List<MenuProductDto> getMenuProductDtos() {
        return menuProductDtos;
    }

    public Menu toMenuToSave(MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }

    public List<MenuProduct> makeMenuProductToSave() {
        return menuProductDtos.stream()
                .map(menuProductDto -> new MenuProduct(
                        menuProductDto.getProductId(),
                        menuProductDto.getQuantity()))
                .collect(Collectors.toList());
    }
}
