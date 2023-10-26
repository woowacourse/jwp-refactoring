package kitchenpos.menu.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuName {

    @Column(nullable = false)
    private String value;

    protected MenuName() {
    }

    private MenuName(final String value) {
        validate(value);
        this.value = value;
    }

    public static MenuName from(final String value) {
        return new MenuName(value);
    }

    private static void validate(final String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("메뉴 이름은 필수 항목입니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
