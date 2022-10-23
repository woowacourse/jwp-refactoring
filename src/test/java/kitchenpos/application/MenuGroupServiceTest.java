package kitchenpos.application;

import static java.util.stream.Collectors.*;
import static kitchenpos.application.fixture.MenuGroupFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
class MenuGroupServiceTest {

    private final MenuGroupService menuGroupService;

    @Autowired
    public MenuGroupServiceTest(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @DisplayName("menuGroup을 생성한다.")
    @Test
    void menuGroup을_생성한다() {
        MenuGroup 신메뉴 = 신메뉴();

        MenuGroup actual = menuGroupService.create(신메뉴);

        assertThat(actual.getName())
                .isEqualTo(신메뉴.getName());
    }

    @DisplayName("menuGroup list를 조회한다.")
    @Test
    void menuGroup_list를_조회한다() {
        MenuGroup 두마리메뉴 = menuGroupService.create(두마리메뉴());
        MenuGroup 한마리메뉴 = menuGroupService.create(한마리메뉴());
        MenuGroup 순살파닭두마리메뉴 = menuGroupService.create(순살파닭두마리메뉴());

        List<String> actual = menuGroupService.list()
                .stream()
                .map(MenuGroup::getName)
                .collect(toList());

        assertAll(() -> {
            assertThat(actual).hasSize(3);
            assertThat(actual)
                    .containsExactly(두마리메뉴.getName(), 한마리메뉴.getName(), 순살파닭두마리메뉴.getName());
        });
    }
}
