package kitchenpos.fixture;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;

public enum MenuGroupFixtures {

    두마리메뉴_그룹(1L, "두마리메뉴"),
    한마리메뉴_그룹(2L, "한마리메뉴"),
    순살파닭두마리메뉴_그룹(3L, "순살파닭두마리메뉴"),
    신메뉴_그룹(4L, "신메뉴"),
    ;

    private final Long id;
    private final String name;

    MenuGroupFixtures(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static List<MenuGroup> 메뉴_그룹_목록_조회() {
        return Arrays.stream(MenuGroupFixtures.values())
                .map(fixture -> new MenuGroup(fixture.getId(), fixture.getName()))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
