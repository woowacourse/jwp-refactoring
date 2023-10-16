package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest extends ServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Nested
    class create_성공_테스트 {

        @Test
        void 메뉴_그룹을_생성할_수_있다() {
            // given
            final var menuGroup = new MenuGroup("메뉴_그룹_이름");
            menuGroup.setId(1L);
            given(menuGroupDao.save(menuGroup)).willReturn(menuGroup);

            // when
            final var actual = menuGroupService.create(menuGroup);

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(menuGroup);
        }
    }

    @Nested
    class create_실패_테스트 {
    }

    @Nested
    class list_성공_테스트 {

        @Test
        void 메뉴_그룹이_존재하지_않으면_빈_값을_반환한다() {
            // given
            given(menuGroupDao.findAll()).willReturn(Collections.emptyList());

            // when
            final var actual = menuGroupService.list();

            // then
            assertThat(actual).isEmpty();
        }

        @Test
        void 메뉴_그룹이_하나_이상_존재하면_메뉴_그룹_목록을_반환한다() {
            // given
            final var menuGroup = new MenuGroup("메뉴_그룹_이름");
            given(menuGroupDao.findAll()).willReturn(List.of(menuGroup));

            final var expected = List.of(menuGroup);

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
