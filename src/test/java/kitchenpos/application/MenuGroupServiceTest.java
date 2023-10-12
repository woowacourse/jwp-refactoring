package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Nested
    class 메뉴_그룹_등록 {

        @Test
        void 메뉴_그룹을_등록할_수_있다() {
            // given
            final var menuGroup = MenuGroupFixture.메뉴그룹_신메뉴();
            given(menuGroupDao.save(any()))
                    .willReturn(menuGroup);

            // when
            final var actual = menuGroupService.create(menuGroup);

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(menuGroup);
        }
    }

    @Nested
    class 메뉴_그룹_목록_조회 {

        @Test
        void 메뉴_그룹을_조회할_수_있다() {
            // given
            final var menuGroups = Collections.singletonList(MenuGroupFixture.메뉴그룹_신메뉴());
            given(menuGroupDao.findAll())
                    .willReturn(menuGroups);

            // when
            final var actual = menuGroupService.list();

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(menuGroups);
        }
    }
}
