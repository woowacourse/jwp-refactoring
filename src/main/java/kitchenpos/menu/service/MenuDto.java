package kitchenpos.menu.service;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.menu.domain.Menu;

public class MenuDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductDto> menuProductDtos;

    public static MenuDto from(Menu entity) {
        List<MenuProductDto> menuProductDtos = entity.getMenuProducts()
                                                     .stream()
                                                     .map(menuProduct -> MenuProductDto.from(menuProduct, entity.getId()))
                                                     .collect(toList());

        MenuDto menuDto = new MenuDto();
        menuDto.setId(entity.getId());
        menuDto.setName(entity.getName());
        menuDto.setPrice(entity.getPrice());
        menuDto.setMenuGroupId(entity.getMenuGroupId());
        menuDto.setMenuProductDtos(menuProductDtos);
        return menuDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProductDto> getMenuProductDtos() {
        return menuProductDtos;
    }

    public void setMenuProductDtos(final List<MenuProductDto> menuProductDtos) {
        this.menuProductDtos = menuProductDtos;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        MenuDto menuDto = (MenuDto) object;
        return Objects.equals(id, menuDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
