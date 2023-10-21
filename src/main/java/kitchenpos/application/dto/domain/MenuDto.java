package kitchenpos.application.dto.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;

public class MenuDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private List<MenuProductDto> menuProducts;

    public static MenuDto from(final Menu menu) {
        return new MenuDto(menu.getId(), menu.getName(), menu.getPrice().getValue(),
                menu.getMenuProducts().stream().map(MenuProductDto::from).collect(Collectors.toList()));
    }

    public MenuDto(final Long id, final String name, final BigDecimal price, final List<MenuProductDto> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuProducts = menuProducts;
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

    public List<MenuProductDto> getMenuProducts() {
        return menuProducts;
    }

}
