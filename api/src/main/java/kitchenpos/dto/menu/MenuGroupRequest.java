package kitchenpos.dto.menu;

import kitchenpos.domain.menu.MenuGroup;

import javax.validation.constraints.NotBlank;

public class MenuGroupRequest {
    @NotBlank(message = "메뉴 그룹명이 비어있거나, null입니다.")
    private String name;

    private MenuGroupRequest() {
    }

    private MenuGroupRequest(String name) {
        this.name = name;
    }

    public static MenuGroupRequest from(String name) {
        return new MenuGroupRequest(name);
    }

    public String getName() {
        return name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }
}
