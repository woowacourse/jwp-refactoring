package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.micrometer.core.instrument.util.StringUtils;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public MenuGroup() {
    }

    public MenuGroup(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("메뉴그룹 이름을 입력해주세요");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
