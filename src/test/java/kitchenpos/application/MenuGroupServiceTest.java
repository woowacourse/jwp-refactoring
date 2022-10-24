package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 테이블_그룹을_생성한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("테스트메뉴");

        // when, then
        assertThat(menuGroupService.create(menuGroup).getName()).isEqualTo("테스트메뉴");
    }

    @Test
    void 테이블_그룹_전체_조회를_한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("테스트메뉴");
        menuGroupService.create(menuGroup);

        // when, then
        final List<String> menuGroupNames = menuGroupService.list()
                .stream()
                .map(MenuGroup::getName)
                .collect(Collectors.toList());

        assertThat(menuGroupNames).contains("테스트메뉴");
    }
}
