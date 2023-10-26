package kitchenpos.menu;

import java.util.List;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.menugroup.CreateMenuGroupRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_저장할_수_있다() {
        // given
        final CreateMenuGroupRequest createMenuGroupRequest = new CreateMenuGroupRequest("피자");

        // when
        final MenuGroup actual = menuGroupService.create(createMenuGroupRequest);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("피자")
        );
    }

    @Test
    void 저장된_메뉴_그룹을_모두_가져올_수_있다() {
        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual).hasSize(4);
    }

}
