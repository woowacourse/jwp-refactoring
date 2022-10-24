package kitchenpos.application;

import static kitchenpos.support.MenuGroupFixtures.createAll;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("menuGroup을 생성한다.")
    @Test
    void create() {
        // given
        final MenuGroup menuGroup = new MenuGroup("세마리메뉴");

        // when
        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @DisplayName("menuGroup들을 조회한다.")
    @Test
    void list() {
        // given
        final List<MenuGroup> expected = createAll();

        // when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
