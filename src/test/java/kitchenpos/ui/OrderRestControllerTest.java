package kitchenpos.ui;

import io.restassured.RestAssured;
import kitchenpos.application.OrderService;
import kitchenpos.common.controller.ControllerTest;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static io.restassured.http.ContentType.JSON;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

class OrderRestControllerTest extends ControllerTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void Order를_생성하면_201을_반환한다() {
        // given
        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 0, false));
        final Order 주문 = new Order(orderTable.getId(), null, LocalDateTime.now());
        final var 요청_준비 = RestAssured.given()
                .body(주문)
                .contentType(JSON);

        // when
        final var 응답 = 요청_준비
                .when()
                .post("/api/orders");

        // then
        응답.then().assertThat().statusCode(CREATED.value());
    }

    @Test
    void Order를_조회하면_200을_반환한다() {
        // given
        final var 요청_준비 = RestAssured.given()
                .contentType(JSON);

        // when
        final var 응답 = 요청_준비
                .when()
                .get("/api/orders");

        // then
        응답.then().assertThat().statusCode(OK.value());
    }

    @Test
    void 주문상태를_변경하면_200을_반환한다() {
        //given
        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now(), null));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 0, false));
        final Order 주문 = orderService.create(new Order(orderTable.getId(), null, LocalDateTime.now()));

        final var 요청_준비 = RestAssured.given()
                .body(주문)
                .contentType(JSON);

        // when
        final var 응답 = 요청_준비
                .when()
                .put("/api/orders/" + 주문.getId() + "/order-status");

        // then
        응답.then().assertThat().statusCode(OK.value());
    }
}
