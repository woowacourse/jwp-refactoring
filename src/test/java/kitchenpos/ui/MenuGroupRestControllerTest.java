package kitchenpos.ui;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.utils.TestFixture;

class MenuGroupRestControllerTest extends ControllerTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("create: 메뉴 그룹 등록 테스트")
    @Test
    void createTest() throws Exception {
        final MenuGroup menuGroup = TestFixture.getMenuGroup("한마리메뉴");

        create("/api/menu-groups", menuGroup)
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name").value(menuGroup.getName()));
    }

    @DisplayName("list: 전체 메뉴 그룹 조회 테스트")
    @Test
    void listTest() throws Exception {
        menuGroupDao.save(TestFixture.getMenuGroup("한마리메뉴"));
        menuGroupDao.save(TestFixture.getMenuGroup("두마리메뉴"));

        findList("/api/menu-groups")
                .andExpect(jsonPath("$[0].name").value("두마리메뉴"))
                .andExpect(jsonPath("$[1].name").value("한마리메뉴"));
    }
}
