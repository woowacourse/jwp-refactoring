package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {

    private static final int MAX_NAME_LENGTH = 255;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
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
            throw new IllegalArgumentException();
        }

        if (name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
