package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void create() {
        final MenuGroup menuGroup = new MenuGroup("test menuGroup");

        final MenuGroup actual = menuGroupService.create(menuGroup);

        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("메뉴 그룹 리스트로 반환한다.")
    void list() {
        final List<MenuGroup> actual = menuGroupService.list();

        assertThat(actual).hasSize(4);
    }
}
