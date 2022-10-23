package kitchenpos.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;

@SpringBootTest
public class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴를 등록한다.")
    void create() {
        // given
        MenuGroup menuGroup = new MenuGroup("세마리세트");

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(savedMenuGroup).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(menuGroup);
    }

    @Test
    @DisplayName("전체 메뉴를 조회한다.")
    void list() {
        // given
        MenuGroup menuGroup = new MenuGroup("세마리세트");
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // when
        List<MenuGroup> result = menuGroupService.list();

        // then
        assertThat(result).contains(savedMenuGroup);
    }
}
