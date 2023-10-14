package kitchenpos.application;

import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fake.InMemoryMenuGroupDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest {

    private MenuGroupService menuGroupService;
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void before() {
        menuGroupDao = new InMemoryMenuGroupDao();
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @Test
    void 주문_그룹을_생성한다() {
        // given
        MenuGroupRequest menuGroup = new MenuGroupRequest("korean");

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
            softly.assertThat(savedMenuGroup.getId()).isNotNull();
        });
    }

    @Test
    void 주문을_전체_조회한다() {
        // given
        menuGroupDao.save(new MenuGroup("korean"));
        menuGroupDao.save(new MenuGroup("french"));

        // when
        List<MenuGroup> savedMenuGroups = menuGroupService.list();

        // then
        assertThat(savedMenuGroups).map(MenuGroup::getName)
                .usingRecursiveComparison()
                .isEqualTo(List.of("korean", "french"));
    }
}
