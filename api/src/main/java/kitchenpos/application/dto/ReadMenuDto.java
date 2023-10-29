package kitchenpos.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;

public class ReadMenuDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<ReadMenuProductDto> menuProducts;

    public ReadMenuDto(final Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.menuGroupId = menu.getMenuGroupId();
        this.menuProducts = convertCreateMenuProductDto(menu);
    }

    private List<ReadMenuProductDto> convertCreateMenuProductDto(final Menu menu) {
        return menu.getMenuProducts()
                   .stream()
                   .map(ReadMenuProductDto::new)
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

    public List<ReadMenuProductDto> getMenuProducts() {
        return menuProducts;
    }
}
