package kitchenpos.application;

import static kitchenpos.application.TestFixture.메뉴_그룹_생성;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 테이블_그룹을_생성한다() {
        // given, when
        final MenuGroup actual = menuGroupService.create(메뉴_그룹_생성("테스트-메뉴-그룹"));

        // then
        assertThat(actual.getName()).isEqualTo("테스트-메뉴-그룹");
    }

    @Test
    void 테이블_그룹_전체를_조회한다() {
        // given
        final MenuGroup expected = 메뉴_그룹을_저장한다(메뉴_그룹_생성("테스트-메뉴-그룹"));

        // when
        final List<MenuGroup> actual = menuGroupService.list();

        //then
        assertThat(actual).usingElementComparatorOnFields("name")
                .containsExactly(expected);
    }
}
