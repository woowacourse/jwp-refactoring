package kitchenpos.acceptance;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 관련 기능")
class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Autowired
    MenuGroupDao menuGroupDao;

    MenuGroup 한마리메뉴 = new MenuGroup();
    MenuGroup 두마리메뉴 = new MenuGroup();

    @BeforeEach
    void setUp() {
        한마리메뉴.setName("한마리메뉴");
        menuGroupDao.save(한마리메뉴);

        두마리메뉴.setName("두마리메뉴");
        menuGroupDao.save(두마리메뉴);
    }

    @DisplayName("등록된 전체 메뉴 그룹 카테코리를 반환한다")
    @Test
    void getMenuGroups() {
        // when
        ResponseEntity<MenuGroup[]> responseEntity = testRestTemplate.getForEntity("/api/menu-groups", MenuGroup[].class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(2);
    }

    @DisplayName("메뉴 그룹 카테고리를 생성한다")
    @Test
    void createMenuGroup() {
        // given
        MenuGroup 세마리메뉴 = new MenuGroup();
        세마리메뉴.setName("세마리메뉴");

        // when
        ResponseEntity<MenuGroup> responseEntity = testRestTemplate.postForEntity("/api/menu-groups", 세마리메뉴, MenuGroup.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        MenuGroup 응답_Menu_Group = responseEntity.getBody();
        assertThat(응답_Menu_Group.getName()).isEqualTo("세마리메뉴");
    }
}
