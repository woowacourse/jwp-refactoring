package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceTest {

    @Nested
    class create_성공_테스트 {

        @Test
        void 메뉴_그룹을_생성할_수_있다() {
            // given
            final var request = new MenuGroupCreateRequest("메뉴_그룹_이름");

            // when
            final var actual = menuGroupService.create(request);

            // then
            assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
        }
    }

    @Nested
    class create_실패_테스트 {
    }

    @Nested
    class list_성공_테스트 {

        @Test
        void 메뉴_그룹이_존재하지_않으면_빈_값을_반환한다() {
            // given & when
            final var actual = menuGroupService.list();

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void 메뉴_그룹이_하나_이상_존재하면_메뉴_그룹_목록을_반환한다() {
            // given
            final var menuGroup = menuGroupRepository.save(new MenuGroup("메뉴_그룹_이름"));

            final var expected = List.of(MenuGroupResponse.from(menuGroup));

            // when
            final var actual = menuGroupService.list();

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    @Nested
    class list_실패_테스트 {
    }

}
