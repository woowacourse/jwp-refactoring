package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.ProductFixture;

@JdbcTest
@Sql("classpath:/truncate.sql")
class OrderLineItemDaoTest {

    @Autowired
    DataSource dataSource;

    OrderLineItemDao orderLineItemDao;

    OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        orderLineItemDao = new JdbcTemplateOrderLineItemDao(dataSource);

        orderLineItem = OrderLineItemFixture.createWithoutId(MenuFixture.ID1, OrderFixture.ID1);
    }

    @DisplayName("Insert a order line item")
    @Test
    void save() {
        OrderLineItem saved = orderLineItemDao.save(orderLineItem);

        assertThat(saved).isEqualToIgnoringGivenFields(orderLineItem, "seq");
        assertThat(saved).extracting(OrderLineItem::getSeq).isEqualTo(1L);
    }

    @DisplayName("Select a order line item")
    @Test
    void findById() {
        OrderLineItem saved = orderLineItemDao.save(orderLineItem);

        assertThat(orderLineItemDao.findById(saved.getSeq()).get())
            .isEqualToComparingFieldByField(saved);
    }

    @DisplayName("Select all order line items")
    @Test
    void findAll() {
        OrderLineItem saved1 = orderLineItemDao.save(orderLineItem);
        OrderLineItem saved2 = orderLineItemDao.save(orderLineItem);

        assertThat(orderLineItemDao.findAll())
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(saved1, saved2));
    }

    @DisplayName("Select all order line items by order id")
    @Test
    void findAllByOrderId() {
        OrderLineItem saved1 = orderLineItemDao.save(orderLineItem);
        OrderLineItem saved2 = orderLineItemDao.save(orderLineItem);

        assertThat(orderLineItemDao.findAllByOrderId(saved1.getOrderId()))
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(saved1, saved2));
    }
}
