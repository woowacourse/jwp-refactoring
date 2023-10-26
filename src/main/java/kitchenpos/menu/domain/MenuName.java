package kitchenpos.menu.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import org.springframework.util.StringUtils;

@Embeddable
public class MenuName {

    private static final int MAX_NAME_LENGTH = 255;

    @NotNull
    private String name;

    protected MenuName() {
    }

    private MenuName(String name) {
        validate(name);

        this.name = name;
    }

    private void validate(String name) {
        if (!StringUtils.hasText(name) || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("메뉴 이름의 길이는 1글자 이상, 255글자 이하여야 합니다.");
        }
    }

    public static MenuName from(String name) {
        return new MenuName(name);
    }

    public String getName() {
        return name;
    }

}
