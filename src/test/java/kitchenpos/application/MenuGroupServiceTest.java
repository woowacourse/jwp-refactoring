package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import kitchenpos.application.menugroup.MenuGroupService;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Nested
    class 메뉴_그룹_등록 {

        @Test
        void 메뉴_그룹을_등록할_수_있다() {
            // given
            final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
            final var request = MenuGroupFixture.메뉴그룹요청_생성(menuGroup);

            // when
            final var actual = menuGroupService.create(request);

            // then
            final var expected = MenuGroupResponse.toResponse(menuGroup);
            assertThat(actual).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expected);
        }
    }

    @Nested
    class 메뉴_그룹_목록_조회 {

        @Test
        void 메뉴_그룹을_조회할_수_있다() {
            // given
            final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
            단일_메뉴그룹_저장(menuGroup);

            // when
            final var actual = menuGroupService.list();

            // then
            final var expected = Collections.singletonList(MenuGroupResponse.toResponse(menuGroup));
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }
}
