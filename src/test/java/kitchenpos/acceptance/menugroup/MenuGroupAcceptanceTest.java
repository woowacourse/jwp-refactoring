package kitchenpos.acceptance.menugroup;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹 등록 성공")
    @Test
    void create() {
        MenuGroup recommendation = new MenuGroup();
        recommendation.setName("추천메뉴");

        ResponseEntity<MenuGroup> responseEntity = testRestTemplate.postForEntity(
                "/api/menu-groups",
                recommendation,
                MenuGroup.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    void list() {
        MenuGroup recommendation = new MenuGroup();
        recommendation.setName("추천메뉴");
        MenuGroup best = new MenuGroup();
        best.setName("최고메뉴");

        menuGroupDao.save(recommendation);
        menuGroupDao.save(best);

        ResponseEntity<List> responseEntity = testRestTemplate.getForEntity(
                "/api/menu-groups",
                List.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(2);
    }
}
