package kitchenpos.application;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroupFixture.음료();
    }

    @Test
    void 메뉴그룹을_등록한다() {
        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(savedMenuGroup).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(menuGroup);
    }

    @Test
    void 전체_메뉴그룹을_조회한다() {
        // given
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups.get(menuGroups.size() - 1))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(savedMenuGroup);
    }

}
