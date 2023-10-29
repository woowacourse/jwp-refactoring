package kitchenpos.core.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuName {

    private static final int NAME_LENGTH_MAXIMUM = 255;

    @Column(nullable = false)
    private String name;

    protected MenuName() {
    }

    private MenuName(final String name) {
        validate(name);
        this.name = name;
    }

    public static MenuName from(final String name) {
        return new MenuName(name);
    }

    private static void validate(final String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("메뉴 이름은 필수 항목입니다.");
        }
        if (name.length() > NAME_LENGTH_MAXIMUM) {
            throw new IllegalArgumentException("메뉴 이름의 최대 길이는 " + NAME_LENGTH_MAXIMUM + "입니다.");
        }
    }

    public String getName() {
        return name;
    }
}
