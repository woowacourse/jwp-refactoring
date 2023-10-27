package kitchenpos.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;

public class CreateMenuDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<CreateMenuProductDto> menuProducts;

    public CreateMenuDto(final Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.menuGroupId = menu.getMenuGroupId();
        this.menuProducts = convertCreateMenuProductDto(menu);
    }

    private List<CreateMenuProductDto> convertCreateMenuProductDto(final Menu menu) {
        return menu.getMenuProducts()
                   .stream()
                   .map(CreateMenuProductDto::new)
                   .collect(Collectors.toList());
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

    public List<CreateMenuProductDto> getMenuProducts() {
        return menuProducts;
    }
}
