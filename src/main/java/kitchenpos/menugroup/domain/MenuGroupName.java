package kitchenpos.menugroup.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import org.springframework.util.StringUtils;

@Embeddable
public class MenuGroupName {

    private static final int MAX_NAME_LENGTH = 255;
    @NotNull
    private String name;

    protected MenuGroupName() {
    }

    private MenuGroupName(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (!StringUtils.hasText(name) || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("메뉴 그룹 이름은 1글자 이상, 255자 이하여야 합니다.");
        }
    }

    public static MenuGroupName from(String name) {
        return new MenuGroupName(name);
    }

    public String getName() {
        return name;
    }

}
