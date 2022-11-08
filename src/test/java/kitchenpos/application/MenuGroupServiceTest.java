package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.dto.MenuGroupRequestDto;
import kitchenpos.menu.application.dto.MenuGroupResponse;
import kitchenpos.menu.domain.MenuGroup;
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

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void create() {
        final MenuGroupRequestDto menuGroup = new MenuGroupRequestDto("test menuGroup");

        final MenuGroup actual = menuGroupService.create(menuGroup);

        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("메뉴 그룹 리스트로 반환한다.")
    void list() {
        final MenuGroupRequestDto menuGroup = new MenuGroupRequestDto("test menuGroup");
        menuGroupService.create(menuGroup);
        menuGroupService.create(menuGroup);
        menuGroupService.create(menuGroup);

        List<MenuGroupResponse> actual = menuGroupService.list();

        assertThat(actual).hasSize(3);
    }
}
