package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Nested
    class Create {

        @Test
        void 메뉴_그룹을_생성할_수_있다() {
            // given
            final MenuGroup expected = new MenuGroup("식사");
            given(menuGroupDao.save(any(MenuGroup.class))).willReturn(expected);

            // when
            final MenuGroup actual = menuGroupService.create(new MenuGroup());

            // then
            assertThat(actual.getName()).isEqualTo(expected.getName());
        }
    }

    @Nested
    class FindAll {

        @Test
        void 메뉴_그룹을_전체_조회할_수_있다() {
            // given
            final MenuGroup siksa = new MenuGroup("식사");
            final MenuGroup noodles = new MenuGroup("면류");
            given(menuGroupDao.findAll()).willReturn(List.of(siksa, noodles));

            // when
            final List<MenuGroup> actual = menuGroupService.list();

            // then
            assertThat(actual).containsExactly(siksa, noodles);
        }
    }
}
