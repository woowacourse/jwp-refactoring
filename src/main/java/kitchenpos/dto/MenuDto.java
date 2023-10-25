package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;

public class MenuDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProductDtos;

    public static MenuDto from(Menu menu) {
        List<MenuProductDto> menuProductDtos = menu.getMenuProducts().stream()
                .map(MenuProductDto::from)
                .collect(Collectors.toList());

        return new MenuDto(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroup().getId(),
                menuProductDtos);
    }

    public MenuDto(String name, BigDecimal price, Long menuGroupId,
            List<MenuProductDto> menuProductDtos) {
        this(null, name, price, menuGroupId, menuProductDtos);
    }

    public MenuDto(Long id, String name, BigDecimal price, Long menuGroupId,
            List<MenuProductDto> menuProductDtos) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductDtos = menuProductDtos;
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

    public List<MenuProductDto> getMenuProductDtos() {
        return menuProductDtos;
    }
}
