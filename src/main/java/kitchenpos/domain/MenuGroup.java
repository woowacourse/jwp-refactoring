package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class MenuGroup {

    private static final int MAX_NAME_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    protected MenuGroup() {
    }

    private MenuGroup(String name) {
        this.name = name;
    }

    public static MenuGroup from(String name) {
        validateName(name);

        return new MenuGroup(name);
    }

    private static void validateName(String name) {
        if (name == null) {
            throw new NullPointerException("메뉴 그룹 이름은 null일 수 없습니다.");
        }
        if (name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("메뉴 그룹 이름은 1글자 이상, 255자 이하여야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
