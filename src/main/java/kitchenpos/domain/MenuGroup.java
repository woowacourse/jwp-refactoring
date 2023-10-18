package kitchenpos.domain;

public class MenuGroup {

    private static final int NAME_LENGTH_MAXIMUM = 255;
    
    private final String name;
    private final Long id;

    public MenuGroup(final Long id, final String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    public MenuGroup(final String name) {
        this(null, name);
    }

    private void validateName(final String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("상품 이름은 필수 항목입니다.");
        }
        if (name.length() > NAME_LENGTH_MAXIMUM) {
            throw new IllegalArgumentException("상품 이름의 최대 길이는 " + NAME_LENGTH_MAXIMUM + "입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
