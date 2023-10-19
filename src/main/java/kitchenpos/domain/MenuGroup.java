package kitchenpos.domain;

public class MenuGroup {

    private Long id;
    private String name;

    public MenuGroup() {
    }

    public MenuGroup(final Long id, final String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    public MenuGroup(final String name) {
        this(null, name);
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("메뉴 그룹의 이름이 비어있습니다.");
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
