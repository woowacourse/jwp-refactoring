package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.supports.MenuGroupFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    MenuGroupDao menuGroupDao;

    @InjectMocks
    MenuGroupService menuGroupService;

    @Nested
    class 메뉴_그룹_생성 {

        @Test
        void 이름을_받아_메뉴_그룹을_생성한다() {
            // given
            String name = "안주류";

            MenuGroup menuGroup = MenuGroupFixture.fixture().name(name).build();
            MenuGroup expected = MenuGroupFixture.fixture().id(1L).name(name).build();

            given(menuGroupService.create(menuGroup))
                .willReturn(expected);

            // when
            MenuGroup actual = menuGroupService.create(menuGroup);

            // then
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    class 메뉴_그룹_전체_조회 {

        @Test
        void 전체_메뉴_그룹을_조회한다() {
            // given
            List<MenuGroup> expected = List.of(
                MenuGroupFixture.fixture().id(1L).build(),
                MenuGroupFixture.fixture().id(2L).build()
            );

            given(menuGroupService.list())
                .willReturn(expected);

            // when
            List<MenuGroup> actual = menuGroupService.list();

            // then
            assertThat(actual).isEqualTo(expected);
        }
    }
}
