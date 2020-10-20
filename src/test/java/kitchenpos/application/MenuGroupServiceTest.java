package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.utils.KitchenPosClassCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Sql("/truncate.sql")
@SpringBootTest
public class MenuGroupServiceTest {
    public static final String TEST_MENU_GROUP_NAME_1 = "두마리 세트";
    public static final String TEST_MENU_GROUP_NAME_2 = "세마리 세트";

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = KitchenPosClassCreator.createMenuGroup(TEST_MENU_GROUP_NAME_1);
    }

    @DisplayName("MenuGroup 생성이 올바르게 수행된다.")
    @Test
    void createTest() {
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        Assertions.assertEquals(savedMenuGroup.getName(), menuGroup.getName());
    }

    @DisplayName("MenuGroup 전체 목록을 요청 시 올바른 값이 반환된다.")
    @Test
    void listTest() {
        MenuGroup secondMenuGroup = KitchenPosClassCreator.createMenuGroup(TEST_MENU_GROUP_NAME_2);
        MenuGroup savedMenuGroup1 = menuGroupDao.save(menuGroup);
        MenuGroup savedMenuGroup2 = menuGroupDao.save(secondMenuGroup);

        List<MenuGroup> foundMenuGroup = menuGroupService.list();

        assertThat(foundMenuGroup)
                .hasSize(2)
                .extracting("id")
                .containsOnly(savedMenuGroup1.getId(), savedMenuGroup2.getId());
    }
}
