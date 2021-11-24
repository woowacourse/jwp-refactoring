package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MenuGroupApiTest extends ApiTest {

    private static final String BASE_URL = "/api/menu-groups";

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private List<MenuGroup> menuGroups;

    @Override
    @BeforeEach
    void setUp() throws SQLException {
        super.setUp();
        menuGroups = new ArrayList<>();

        menuGroups.add(menuGroupRepository.save(new MenuGroup("두마리메뉴")));
        menuGroups.add(menuGroupRepository.save(new MenuGroup("한마리메뉴")));
    }

    @DisplayName("메뉴 그룹 등록")
    @Test
    void postMenuGroup() {
        MenuGroupRequest request = new MenuGroupRequest("추천메뉴");

        ResponseEntity<MenuGroupResponse> responseEntity = testRestTemplate.postForEntity(
            BASE_URL, request, MenuGroupResponse.class
        );
        MenuGroupResponse response = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getId()).isNotNull();
        assertThat(response).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(request);
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void getMenuGroups() {
        ResponseEntity<MenuGroupResponse[]> responseEntity = testRestTemplate.getForEntity(
            BASE_URL, MenuGroupResponse[].class
        );
        MenuGroupResponse[] response = responseEntity.getBody();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response).usingRecursiveFieldByFieldElementComparator()
            .hasSameSizeAs(menuGroups)
            .containsAll(MenuGroupResponse.listFrom(menuGroups));
    }
}
