package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.application.support.domain.MenuGroupTestSupport;
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
        final var origin = MenuGroupTestSupport.builder().id(null).build();
        final var expectedResult = MenuGroupTestSupport.builder().id(1L).name(origin.getName()).build();
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(expectedResult);

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
        final MenuGroup menuGroup1 = MenuGroupTestSupport.builder().build();
        final MenuGroup menuGroup2 = MenuGroupTestSupport.builder().build();

        given(menuGroupDao.findAll()).willReturn(List.of(menuGroup1, menuGroup2));

        //when
        final List<MenuGroup> result = target.list();

        //then
        assertThat(result).contains(menuGroup1, menuGroup2);
    }
}
