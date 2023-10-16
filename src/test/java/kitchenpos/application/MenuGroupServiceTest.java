package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성한다() {
        // given
        MenuGroup menuGroup = MenuGroup.builder()
                .id(1L)
                .name("메뉴 그룹")
                .build();
        given(menuGroupDao.save(any()))
                .willReturn(menuGroup);

        // when & then
        MenuGroup result = menuGroupService.create(menuGroup);
        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(menuGroup);
    }

    @Test
    void 메뉴_그룹_목록을_조회한다() {
        // given
        MenuGroup menuGroup = MenuGroup.builder()
                .id(1L)
                .name("메뉴 그룹")
                .build();
        given(menuGroupDao.findAll())
                .willReturn(List.of(menuGroup));

        // when & then
        Assertions.assertThat(menuGroupService.list())
                .usingRecursiveComparison()
                .isEqualTo(List.of(menuGroup));
    }
}
