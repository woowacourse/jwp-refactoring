package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 생성한다")
    void create() {
        // given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("메뉴 그룹");

        // when
        final MenuGroup actual = menuGroupService.create(menuGroup);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("메뉴 그룹")
        );
    }

    @Test
    @DisplayName("모든 메뉴 그룹을 조회한다")
    void list() {
        // given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("메뉴 그룹");
        final Long savedId = menuGroupService.create(menuGroup)
                .getId();

        // when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(menuGroups).extracting("id")
                        .contains(savedId),
                () -> assertThat(menuGroups).hasSizeGreaterThanOrEqualTo(1)
        );
    }
}
