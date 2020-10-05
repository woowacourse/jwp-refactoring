package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql({"/truncate.sql", "/init-data.sql"})
public class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천 메뉴");

        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertThat(savedMenuGroup).isNotNull();
        assertThat(savedMenuGroup.getId()).isNotNull();
        assertThat(savedMenuGroup.getName()).isEqualTo("추천 메뉴");
    }

    @DisplayName("메뉴 그룹 전체 조회")
    @Test
    void findAll() {
        List<MenuGroup> list = menuGroupService.list();

        assertThat(list).isNotEmpty();
    }
}
