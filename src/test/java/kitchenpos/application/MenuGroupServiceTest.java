package kitchenpos.application;

import kitchenpos.dto.MenuGroupDto;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @MockBean
    private MenuGroupDao menuGroupDao;

    @Test
    void 메뉴_그룹을_생성한다() {
        // given
        MenuGroupDto menuGroupRequest = new MenuGroupDto("menuGroup");

        MenuGroup expected = new MenuGroup(1L, menuGroupRequest.getName());

        given(menuGroupDao.save(any()))
                .willReturn(expected);

        // when
        MenuGroupDto result = menuGroupService.create(menuGroupRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(expected.getId());
            softly.assertThat(result.getName()).isEqualTo(expected.getName());
        });
    }

    @Test
    void 메뉴_그룹을_전체_조회한다() {
        // given
        MenuGroup menuGroup1 = new MenuGroup("menuGroup1");
        MenuGroup menuGroup2 = new MenuGroup("menuGroup2");
        List<MenuGroup> expected = List.of(menuGroup1, menuGroup2);

        given(menuGroupDao.findAll())
                .willReturn(expected);

        // when
        List<MenuGroupDto> result = menuGroupService.list();

        // then
        assertThat(result).hasSize(2);
    }
}
