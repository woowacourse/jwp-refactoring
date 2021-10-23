package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MenuGroupService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @DisplayName("create 메서드는")
    @Nested
    class Describe_create {

        @DisplayName("주어진 MenuGroup을 DB에 저장하고 ID를 포함한 Entity를 반환한다.")
        @Test
        void it_saves_and_returns_menuGroupEntity() {
            // given
            MenuGroup menuGroup = new MenuGroup();
            menuGroup.setName("분식류");
            MenuGroup expected = new MenuGroup();
            expected.setId(1L);
            expected.setName("분식류");
            given(menuGroupDao.save(menuGroup)).willReturn(expected);

            // when
            MenuGroup response = menuGroupService.create(menuGroup);

            // then
            assertThat(response).isEqualTo(expected);

            verify(menuGroupDao, times(1)).save(menuGroup);
        }
    }

    @DisplayName("list 메서드는")
    @Nested
    class Describe_list {

        @DisplayName("DB에 저장된 MenuGroup 목록을 반환한다.")
        @Test
        void it_returns_menuGroupList() {
            // given
            MenuGroup menuGroup = new MenuGroup();
            menuGroup.setId(1L);
            menuGroup.setName("분식류");
            MenuGroup menuGroup2 = new MenuGroup();
            menuGroup2.setId(2L);
            menuGroup2.setName("안주류");
            List<MenuGroup> expected = Arrays.asList(menuGroup, menuGroup2);
            given(menuGroupDao.findAll()).willReturn(expected);

            // when
            List<MenuGroup> response = menuGroupService.list();

            // then
            assertThat(response).isEqualTo(expected);

            verify(menuGroupDao, times(1)).findAll();
        }
    }
}
