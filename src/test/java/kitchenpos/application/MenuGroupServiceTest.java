package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("MenuGroupService를 테스트한다.")
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao productDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private MenuDao menuDao;

    @BeforeEach
    void setUp() {
        productDao.deleteAll();
        orderLineItemDao.deleteAll();
        menuDao.deleteAll();
        menuGroupDao.deleteAll();
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        //given
        MenuGroup menuGroup = new MenuGroup(1L, "추천 메뉴");

        //when
        MenuGroup result = menuGroupService.create(menuGroup);

        //then
        assertThat(result.getName()).isEqualTo("추천 메뉴");
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        //given
        menuGroupService.create(new MenuGroup(1L, "추천 메뉴"));
        menuGroupService.create(new MenuGroup(2L, "할인 메뉴"));
        menuGroupService.create(new MenuGroup(3L, "선착순 메뉴"));

        //when
        List<MenuGroup> list = menuGroupService.list();

        //then
        assertThat(list.size()).isEqualTo(3);
    }

}