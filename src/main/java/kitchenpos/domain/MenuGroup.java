package kitchenpos.domain;

import static java.util.Objects.isNull;

public class MenuGroup {
    private Long id;
    private String name;

    public MenuGroup(final String name) {
        if (isNull(name)) {
            throw new IllegalArgumentException("메뉴 그룹의 이름은 필수로 입력해야 합니다.");
        }
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

}
