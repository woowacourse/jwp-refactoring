package kitchenpos.config;

import kitchenpos.domain.vo.Money;
import kitchenpos.domain.vo.NumberOfGuest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class JdbcConfig extends AbstractJdbcConfiguration {
    @Bean
    @Override
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(List.of(
                new MoneyBigdecimalWritingConverter(),
                new MoneyBigDeciamlReadingConverter(),
                new NumberOfGuestsIntegerWritingConverter(),
                new NumberOfGuestsIntegerReadingConverter()
        ));
    }

    @WritingConverter
    static class MoneyBigdecimalWritingConverter implements Converter<Money, BigDecimal> {

        @Override
        public BigDecimal convert(Money source) {
            return source.getValue();
        }
    }

    @ReadingConverter
    static class MoneyBigDeciamlReadingConverter implements Converter<BigDecimal, Money> {

        @Override
        public Money convert(BigDecimal source) {
            return new Money(source);
        }
    }

    @WritingConverter
    static class NumberOfGuestsIntegerWritingConverter implements Converter<NumberOfGuest, Integer> {

        @Override
        public Integer convert(NumberOfGuest source) {
            return source.getValue();
        }
    }

    @ReadingConverter
    static class NumberOfGuestsIntegerReadingConverter implements Converter<Integer, NumberOfGuest> {

        @Override
        public NumberOfGuest convert(Integer source) {
            return new NumberOfGuest(source);
        }
    }
}
