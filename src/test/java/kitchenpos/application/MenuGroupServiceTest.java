package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.config.ServiceTest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.exception.InvalidNameException;
import kitchenpos.ui.dto.request.CreateMenuGroupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest {

    @Autowired
    MenuGroupService menuGroupService;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Test
    void create_메서드는_menuGroup을_전달하면_menuGroup을_저장하고_반환한다() {
        // given
        final CreateMenuGroupRequest request = new CreateMenuGroupRequest("메뉴 그룹");

        // when
        final MenuGroup actual = menuGroupService.create(request);

        // then
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(request.getName()),
                () -> assertThat(actual.getId()).isPositive()
        );
    }

    @Test
    void create_메서드는_name이_초기화되지_않은_menuGroup을_전달하면_예외가_발생한다() {
        // given
        final CreateMenuGroupRequest invalidRequest = new CreateMenuGroupRequest(null);

        // when & then
        assertThatThrownBy(() -> menuGroupService.create(invalidRequest))
                .isInstanceOf(InvalidNameException.class);
    }

    @Test
    void list_메서드는_등록한_모든_menuGroup을_반환한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        menuGroupRepository.save(menuGroup);

        // when
        final List<MenuGroup> actual = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).getName()).isEqualTo(menuGroup.getName())
        );
    }
}
