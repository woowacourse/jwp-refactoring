package kitchenpos.dto.menugroup;

import kitchenpos.domain.MenuGroup;

import javax.validation.constraints.NotBlank;

public class MenuGroupCreateRequest {
    @NotBlank(message = "메뉴 그룹의 이름은 반드시 존재해야 합니다!")
    private String name;

    public MenuGroupCreateRequest() {
    }

    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(this.name);
    }

    public String getName() {
        return name;
    }
}
