package kitchenpos.application;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

//    @Mock
//    private MenuGroupDao menuGroupDao;
//
//    @InjectMocks
//    private MenuGroupService menuGroupService;
//
//    @DisplayName("메뉴 그룹을 생성할 수 있다.")
//    @Test
//    void create() {
//        MenuGroup menuGroup = createMenuGroup();
//        when(menuGroupDao.save(menuGroup)).thenReturn(createMenuGroup(1L));
//
//        MenuGroup actual = menuGroupService.create(menuGroup);
//
//        assertDoesNotThrow(() -> menuGroupService.create(menuGroup));
//    }
//
//    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
//    @Test
//    void list() {
//        MenuGroup menuGroup1 = createMenuGroup();
//        MenuGroup menuGroup2 = createMenuGroup();
//        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(menuGroup1, menuGroup2));
//
//        List<MenuGroup> actual = menuGroupService.list();
//
//        assertAll(
//                () -> assertThat(actual).hasSize(2),
//                () -> assertThat(actual).containsExactly(menuGroup1, menuGroup2)
//        );
//    }
}
