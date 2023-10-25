package kitchenpos.ui;

import io.restassured.RestAssured;
import kitchenpos.application.OrderService;
import kitchenpos.common.controller.ControllerTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderStatusChangeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.http.ContentType.JSON;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

class OrderRestControllerTest extends ControllerTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void Order를_생성하면_201을_반환한다() {
        // given
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup.getId(), 0, false));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("후라이드"));
        final Menu menu = menuRepository.save(new Menu("디노공룡메뉴", new BigDecimal(17000), menuGroup.getId()));
        final var 요청_준비 = RestAssured.given()
                .body(new OrderCreateRequest(List.of(menu.getId()), List.of(2), orderTable.getId()))
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
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        final OrderTable orderTable = orderTableRepository.save(new OrderTable(tableGroup.getId(), 0, false));
        final MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("후라이드"));
        final Menu menu = menuRepository.save(new Menu("디노공룡메뉴", new BigDecimal(17000), menuGroup.getId()));
        final Long orderId = orderService.create(List.of(menu.getId()), List.of(2), orderTable.getId());

        final var 요청_준비 = RestAssured.given()
                .body(new OrderStatusChangeRequest(OrderStatus.COMPLETION.name()))
                .contentType(JSON);

        // when
        final var 응답 = 요청_준비
                .when()
                .put("/api/orders/" + orderId + "/order-status");

        // then
        응답.then().assertThat().statusCode(OK.value());
    }
}
