package kitchenpos.application;

import kitchenpos.application.dto.MenuGroupRequest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.fake.InMemoryMenuGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.menuGroup;
import static kitchenpos.fixture.MenuGroupFixture.menuGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuGroupServiceTest {

    private MenuGroupService menuGroupService;
    private MenuGroupRepository menuGroupRepository;

    @BeforeEach
    void before() {
        menuGroupRepository = new InMemoryMenuGroupRepository();
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @Test
    void 주문_그룹을_생성한다() {
        // given
        MenuGroupRequest menuGroup = menuGroupRequest("korean");

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
        menuGroupRepository.save(menuGroup("korean"));
        menuGroupRepository.save(menuGroup("french"));

        // when
        List<MenuGroup> savedMenuGroups = menuGroupService.list();

        // then
        assertThat(savedMenuGroups).map(MenuGroup::getName)
                .usingRecursiveComparison()
                .isEqualTo(List.of("korean", "french"));
    }
}
