package kitchenpos.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.generator.MenuGroupGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MenuGroupApiTest extends ApiTest {

    private static final String BASE_URL = "/api/menu-groups";

    @Autowired
    private JdbcTemplateMenuGroupDao menuGroupDao;

    private List<MenuGroup> menuGroups;

    @Override
    @BeforeEach
    void setUp() throws SQLException {
        super.setUp();
        menuGroups = new ArrayList<>();

        menuGroups.add(menuGroupDao.save(MenuGroupGenerator.newInstance("두마리메뉴")));
        menuGroups.add(menuGroupDao.save(MenuGroupGenerator.newInstance("한마리메뉴")));
    }

    @DisplayName("메뉴 그룹 등록")
    @Test
    void postMenuGroup() {
        MenuGroup request = MenuGroupGenerator.newInstance("추천메뉴");

        ResponseEntity<MenuGroup> responseEntity = testRestTemplate.postForEntity(
            BASE_URL,
            request,
            MenuGroup.class
        );
        MenuGroup response = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getId()).isNotNull();
        assertThat(response).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(request);
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void getMenuGroups() {
        ResponseEntity<MenuGroup[]> responseEntity = testRestTemplate.getForEntity(
            BASE_URL,
            MenuGroup[].class
        );
        MenuGroup[] response = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response).usingRecursiveFieldByFieldElementComparator()
            .hasSameSizeAs(menuGroups)
            .containsAll(menuGroups);
    }
}
