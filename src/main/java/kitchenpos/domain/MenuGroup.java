package kitchenpos.domain;

public class MenuGroup {
    private Long id;
    private String name;

    public MenuGroup(String name) {
        validateName(name);
        this.name = name;
    }

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("메뉴그룹의 이름은 비어있을 수 없습니다.");
        }
    }

}
