package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.application.support.dao.MockMenuGroupDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    MenuGroupDao menuGroupDao;

    @InjectMocks
    MenuGroupService target;

    @DisplayName("메뉴 그룹을 생성하면 저장된 메뉴 그룹을 반환한다.")
    @Test
    void create() {
        //given
        final var origin = new MenuGroup();
        final var expectedResult = new MenuGroup();
        expectedResult.setId(1L);
        expectedResult.setName(origin.getName());
        given(menuGroupDao.save(any())).willReturn(expectedResult);

        //when

        final MenuGroup result = target.create(origin);
        //then
        SoftAssertions.assertSoftly(
                soft -> {
                    soft.assertThat(origin.getName()).isEqualTo(result.getName());
                    soft.assertThat(origin.getId()).isNull();
                    soft.assertThat(result.getId()).isNotNull();
                }
        );
    }

    @DisplayName("존재하는 모든 메뉴 그룹을 조회한다.")
    @Test
    void list() {
        //given
        final MenuGroupService menuGroupService = new MenuGroupService(new MockMenuGroupDao());
        final MenuGroup value = new MenuGroup();
        menuGroupService.create(value);

        //when
        final List<MenuGroup> result = menuGroupService.list();

        //then
        assertThat(result).contains(value);
    }
}
