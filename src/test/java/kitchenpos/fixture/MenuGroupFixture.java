package kitchenpos.fixture;

import java.util.function.Consumer;
import kitchenpos.menugroup.MenuGroup;
import kitchenpos.menugroup.MenuGroupDto;

public enum MenuGroupFixture {

    LUNCH(1L, "Lunch Specials");

    private final Long id;
    private final String name;

    MenuGroupFixture(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupDto computeDefaultMenuDto(Consumer<MenuGroupDto> consumer) {
        MenuGroupDto menuGroupDto = new MenuGroupDto();
        menuGroupDto.setId(1L);
        menuGroupDto.setName("Lunch Specials");
        return menuGroupDto;
    }

    public MenuGroupDto toDto() {
        MenuGroupDto menuGroupDto = new MenuGroupDto();
        menuGroupDto.setId(id);
        menuGroupDto.setName(name);
        return menuGroupDto;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(id, name);
    }
}