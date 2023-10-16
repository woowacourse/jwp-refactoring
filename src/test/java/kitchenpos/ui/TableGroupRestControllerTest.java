package kitchenpos.ui;

import io.restassured.RestAssured;
import kitchenpos.application.TableGroupService;
import kitchenpos.common.controller.ControllerTest;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.http.ContentType.JSON;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

class TableGroupRestControllerTest extends ControllerTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void TableGroup을_생성하면_201을_반환한다() {
        final OrderTable orderTable = orderTableDao.save(new OrderTable(null, 0, true));
        final OrderTable orderTable1 = orderTableDao.save(new OrderTable(null, 0, true));
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                List.of(orderTable, orderTable1));
        final var 요청_준비 = RestAssured.given()
                .body(tableGroup)
                .contentType(JSON);

        // when
        final var 응답 = 요청_준비
                .when()
                .post("/api/table-groups");

        // then
        응답.then().assertThat().statusCode(CREATED.value());
    }

    @Test
    void TableGroup을_풀면_204를_반환한다() {
        final OrderTable orderTable = orderTableDao.save(new OrderTable(null, 0, true));
        final OrderTable orderTable1 = orderTableDao.save(new OrderTable(null, 0, true));
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                List.of(orderTable, orderTable1));
        final TableGroup saveTableGroup = tableGroupService.create(tableGroup);
        final var 요청_준비 = RestAssured.given()
                .contentType(JSON);

        // when
        final var 응답 = 요청_준비
                .when()
                .delete("/api/table-groups/" + saveTableGroup.getId());

        // then
        응답.then().assertThat().statusCode(NO_CONTENT.value());
    }
}
