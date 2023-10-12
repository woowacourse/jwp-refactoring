package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Sql("/db/migration/V1__Initialize_project_tables.sql")
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Nested
    class create_성공_테스트 {

        @Test
        void 메뉴_그룹을_생성할_수_있다() {
            // given
            final var menuGroup = new MenuGroup("test");
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
        void 메뉴_그룹이_하나_이상_존재하면_메뉴_그룹_리스트를_반환한다() {
            // given
            final var menuGroup = new MenuGroup("test");
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
