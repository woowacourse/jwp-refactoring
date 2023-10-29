package kitchenpos.menugroup.application;

import kitchenpos.configuration.ServiceTest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.CreateMenuGroupDto;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("메뉴 그룹을 등록한다")
    void create() {
        // given
        final CreateMenuGroupDto request = new CreateMenuGroupDto("추천메뉴");

        // when
        final MenuGroup actual = menuGroupService.create(request);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다")
    void list() {
        // given
        final MenuGroup expect1 = menuGroupRepository.save(new MenuGroup("추천메뉴"));
        final MenuGroup expect2 = menuGroupRepository.save(new MenuGroup("신메뉴"));

        // when
        final List<MenuGroup> actual = menuGroupService.list();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0)).isEqualTo(expect1);
            softAssertions.assertThat(actual.get(1)).isEqualTo(expect2);
        });
    }
}
