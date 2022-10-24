package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("새로운 메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        // given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천 메뉴");

        // when, then
        assertThatCode(() -> menuGroupService.create(menuGroup))
                .doesNotThrowAnyException();
    }

    @DisplayName("메뉴 그룹의 전체 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given, when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).hasSize(4);
    }
}
