package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;

public class MenuDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private List<MenuProductDto> menuProducts;

    public static MenuDto from(final Menu menu) {
        List<MenuProductDto> menuProductDtos = menu.getMenuProducts().stream()
                .map(menuProduct -> MenuProductDto.from(menu.getId(), menuProduct))
                .collect(Collectors.toList());

        return new MenuDto(menu.getId(), menu.getName(), menu.getPrice().getValue(), menuProductDtos);
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
