package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.MenuGroup;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("MenuGroup을 생성한다.")
    void create() {
        //given
        MenuGroup menuGroup = new MenuGroup("test");

        //when
        MenuGroup saved = menuGroupService.create(menuGroup);

        //then
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("리스트 형태로 조회한다.")
    void findAsList() {
        //when
        List<MenuGroup> menus = menuGroupService.list();

        //when
        MenuGroup menuGroup = new MenuGroup("test");
        menuGroupService.create(menuGroup);

        //then
        assertThat(menuGroupService.list()).hasSize(menus.size() + 1);
    }
}
