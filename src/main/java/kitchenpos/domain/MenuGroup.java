package kitchenpos.domain;

import org.springframework.util.StringUtils;

public class MenuGroup {
    private Long id;
    private String name;

    public MenuGroup() {
    }

    public MenuGroup(String name) {
        this.name = name;
        validate();
    }

    private void validate() {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException(String.format("%s : 올바르지 않은 이름입니다.", name));
        }
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

    public void setName(final String name) {
        this.name = name;
    }
}
