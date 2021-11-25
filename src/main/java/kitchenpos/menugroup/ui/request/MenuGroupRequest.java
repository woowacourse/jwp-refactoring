package kitchenpos.menugroup.ui.request;

import javax.validation.constraints.NotBlank;
import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupRequest {

    @NotBlank(message = "메뉴 그룹의 이름이 null이거나 비어있습니다.")
    private String name;

    protected MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }
}
