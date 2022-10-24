package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void create() {
        final MenuGroup menuGroup1 = new MenuGroup();
        menuGroup1.setName("메뉴그룹1");
        final MenuGroup menuGroup2 = new MenuGroup();
        menuGroup2.setName("메뉴그룹2");
        final MenuGroup savedMenuGroup1 = menuGroupService.create(menuGroup1);
        final MenuGroup savedMenuGroup2 = menuGroupService.create(menuGroup2);

        final List<Long> menuGroupIds = menuGroupService.list()
                .stream()
                .map(MenuGroup::getId)
                .collect(Collectors.toList());

        assertThat(menuGroupIds).contains(savedMenuGroup1.getId(), savedMenuGroup2.getId());
    }

    @Test
    @DisplayName("메뉴 그룹은 null일 수 없다.")
    void createWithNullName() {
        final MenuGroup menuGroup = new MenuGroup();

        assertThatThrownBy(() -> menuGroupService.create(menuGroup))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
