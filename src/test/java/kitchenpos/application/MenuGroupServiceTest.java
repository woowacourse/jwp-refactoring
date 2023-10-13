package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.config.ServiceTest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest {

    @Autowired
    MenuGroupService menuGroupService;

    @Autowired
    MenuGroupDao menuGroupDao;

    @Test
    void create_메서드는_menuGroup을_전달하면_menuGroup을_저장하고_반환한다() {
        // given
        final MenuGroup menuGroup = 메뉴_그룹_생성();

        // when
        final MenuGroup actual = menuGroupService.create(menuGroup);

        // then
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(menuGroup.getName()),
                () -> assertThat(actual.getId()).isPositive()
        );
    }

    @Test
    void create_메서드는_name이_초기화되지_않은_menuGroup을_전달하면_예외가_발생한다() {
        // given
        final MenuGroup menuGroup = 메뉴_그룹_생성(null);

        // when & then
        assertThatThrownBy(() -> menuGroupService.create(menuGroup))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void list_메서드는_등록한_모든_menuGroup을_반환한다() {
        // given
        final MenuGroup menuGroup = 메뉴_그룹_생성();
        menuGroupDao.save(menuGroup);

        // when
        final List<MenuGroup> actual = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).getName()).isEqualTo(menuGroup.getName())
        );
    }
}
