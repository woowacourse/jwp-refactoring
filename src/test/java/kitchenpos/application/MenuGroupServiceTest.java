package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    public static final long TEST_MENU_GROUP_ID_1 = 1L;
    public static final long TEST_MENU_GROUP_ID_2 = 2L;
    public static final String TEST_MENU_GROUP_NAME_1 = "두마리 세트";
    public static final String TEST_MENU_GROUP_NAME_2 = "세마리 세트";

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup();
        menuGroup.setId(TEST_MENU_GROUP_ID_1);
        menuGroup.setName(TEST_MENU_GROUP_NAME_1);
    }

    @DisplayName("MenuGroup 생성이 올바르게 수행된다.")
    @Test
    void createTest() {
        when(menuGroupDao.save(any())).thenReturn(menuGroup);

        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        Assertions.assertEquals(savedMenuGroup.getId(), menuGroup.getId());
        Assertions.assertEquals(savedMenuGroup.getName(), menuGroup.getName());
    }

    @DisplayName("MenuGroup 전체 목록을 요청 시 올바른 값이 반환된다.")
    @Test
    void listTest() {
        MenuGroup secondMenuGroup = new MenuGroup();
        secondMenuGroup.setId(TEST_MENU_GROUP_ID_2);
        secondMenuGroup.setName(TEST_MENU_GROUP_NAME_2);
        List<MenuGroup> menuGroups = Arrays.asList(menuGroup, secondMenuGroup);
        when(menuGroupDao.findAll()).thenReturn(menuGroups);

        List<MenuGroup> foundMenuGroup = menuGroupService.list();

        assertThat(foundMenuGroup)
                .hasSize(2)
                .extracting("id")
                .containsOnly(TEST_MENU_GROUP_ID_1, TEST_MENU_GROUP_ID_2);
    }
}
