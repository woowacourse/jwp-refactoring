package kitchenpos.menugroup.application.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menugroup.model.MenuGroup;

public class MenuGroupResponseDto {
    private final Long id;
    private final String name;

    public static MenuGroupResponseDto from(MenuGroup menuGroup) {
        return new MenuGroupResponseDto(menuGroup.getId(), menuGroup.getName());
    }

    public static List<MenuGroupResponseDto> listOf(List<MenuGroup> menuGroups) {
        return menuGroups.stream()
            .map(MenuGroupResponseDto::from)
            .collect(Collectors.toList());
    }

    public MenuGroupResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
