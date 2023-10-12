package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
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

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Test
    void 메뉴_그룹을_생성한다() {
        MenuGroup menuGroup = MenuGroupFixture.메뉴_그룹_엔티티_A;

        given(menuGroupDao.save(any(MenuGroup.class)))
                .willReturn(menuGroup);

        MenuGroup response = menuGroupService.create(menuGroup);

        assertThat(response).usingRecursiveComparison().isEqualTo(menuGroup);
    }

    @Test
    void 모든_메뉴_그룹을_조회한다() {
        MenuGroup menuGroupA = MenuGroupFixture.메뉴_그룹_엔티티_A;
        MenuGroup menuGroupB = MenuGroupFixture.메뉴_그룹_엔티티_B;
        given(menuGroupDao.findAll())
                .willReturn(List.of(menuGroupA, menuGroupB));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).usingRecursiveComparison().isEqualTo(List.of(menuGroupA, menuGroupB));
    }
}
