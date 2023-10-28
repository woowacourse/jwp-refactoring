package kitchenpos.menu.domain.dto.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuName {

    @Column(name = "name")
    private String menuName;

    protected MenuName() {
    }

    public MenuName(String menuName) {
        validate(menuName);
        this.menuName = menuName;
    }

    private void validate(String menuName) {
        if (menuName == null || menuName.isBlank()) {
            throw new IllegalArgumentException("메뉴 이름은 반드시 포함되어야 합니다.");
        }
    }

    public String getValue() {
        return menuName;
    }
}
