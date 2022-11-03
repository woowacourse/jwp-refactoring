package kitchenpos.menu.application.dto;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.ui.dto.request.MenuGroupCreationRequest;

public class MenuGroupCreationDto {

    private final String name;

    private MenuGroupCreationDto(final String name) {
        this.name = name;
    }

    public static MenuGroupCreationDto from(final MenuGroupCreationRequest menuGroupCreationRequest) {
        return new MenuGroupCreationDto(menuGroupCreationRequest.getName());
    }

    public static MenuGroup toEntity(final MenuGroupCreationDto menuGroupCreationDto) {
        return new MenuGroup(menuGroupCreationDto.getName());
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "MenuGroupCreationDto{" +
                "name='" + name + '\'' +
                '}';
    }
}
