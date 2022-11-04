package kitchenpos.application;

import static kitchenpos.support.MenuGroupFixtures.createAll;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("menuGroup을 생성한다.")
    @Test
    void create() {
        // given
        final MenuGroupRequest request = new MenuGroupRequest("메뉴 그룹");

        // when
        final MenuGroup savedMenuGroup = menuGroupService.create(request);

        // then
        assertThat(savedMenuGroup.getId()).isNotNull();
    }

    @DisplayName("menuGroup들을 조회한다.")
    @Test
    void list() {
        // given
        final List<MenuGroup> expected = createAll();

        // when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
