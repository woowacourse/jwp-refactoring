package kitchenpos.dto.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuGroupName {

    @Column(name = "name")
    private String menuGroupName;

    protected MenuGroupName() {
    }

    public MenuGroupName(String menuGroupName) {
        validate(menuGroupName);
        this.menuGroupName = menuGroupName;
    }

    private void validate(String menuName) {
        if (menuName == null || menuName.isBlank()) {
            throw new IllegalArgumentException("메뉴 그룹 이름은 반드시 포함되어야 합니다.");
        }
    }

    public String getValue() {
        return menuGroupName;
    }
}
