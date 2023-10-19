package ai.turintech.catalog.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import io.r2dbc.spi.ConnectionFactoryOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;
import org.springframework.data.r2dbc.query.UpdateMapper;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.relational.core.dialect.RenderContextFactory;
import org.springframework.data.relational.core.sql.render.SqlRenderer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableR2dbcRepositories({ "ai.turintech.catalog.repository" })
@EnableTransactionManagement
public class DatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    private final Environment env;

    public DatabaseConfiguration(Environment env) {
        this.env = env;
    }

    // LocalDateTime seems to be the only type that is supported across all drivers atm
    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions(R2dbcDialect dialect) {
        List<Object> converters = new ArrayList<>();
        converters.add(InstantWriteConverter.INSTANCE);
        converters.add(InstantReadConverter.INSTANCE);
        converters.add(BitSetReadConverter.INSTANCE);
        converters.add(DurationWriteConverter.INSTANCE);
        converters.add(DurationReadConverter.INSTANCE);
        converters.add(ZonedDateTimeReadConverter.INSTANCE);
        converters.add(ZonedDateTimeWriteConverter.INSTANCE);
        return R2dbcCustomConversions.of(dialect, converters);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        String databaseUrl = env.getProperty("spring.r2dbc.url");
        String username = env.getProperty("spring.r2dbc.username");
        String password = env.getProperty("spring.r2dbc.password");

        return ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(DRIVER, "postgresql")
                .option(HOST, "localhost")
                .option(PORT, 5434)
                .option(DATABASE, "postgres")
                .option(USER, username)
                .option(PASSWORD, password)
                .build());
    }

    @Bean
    public R2dbcDialect dialect(ConnectionFactory connectionFactory) {
        return DialectResolver.getDialect(connectionFactory);
    }

    @Bean
    public UpdateMapper updateMapper(R2dbcDialect dialect, MappingR2dbcConverter mappingR2dbcConverter) {
        return new UpdateMapper(dialect, mappingR2dbcConverter);
    }

    @Bean
    public SqlRenderer sqlRenderer(R2dbcDialect dialect) {
        RenderContextFactory factory = new RenderContextFactory(dialect);
        return SqlRenderer.create(factory.createRenderContext());
    }

    @WritingConverter
    public enum InstantWriteConverter implements Converter<Instant, LocalDateTime> {
        INSTANCE;

        public LocalDateTime convert(Instant source) {
            return LocalDateTime.ofInstant(source, ZoneOffset.UTC);
        }
    }

    @ReadingConverter
    public enum InstantReadConverter implements Converter<LocalDateTime, Instant> {
        INSTANCE;

        @Override
        public Instant convert(LocalDateTime localDateTime) {
            return localDateTime.toInstant(ZoneOffset.UTC);
        }
    }

    @ReadingConverter
    public enum BitSetReadConverter implements Converter<BitSet, Boolean> {
        INSTANCE;

        @Override
        public Boolean convert(BitSet bitSet) {
            return bitSet.get(0);
        }
    }

    @ReadingConverter
    public enum ZonedDateTimeReadConverter implements Converter<LocalDateTime, ZonedDateTime> {
        INSTANCE;

        @Override
        public ZonedDateTime convert(LocalDateTime localDateTime) {
            // Be aware - we are using the UTC timezone
            return ZonedDateTime.of(localDateTime, ZoneOffset.UTC);
        }
    }

    @WritingConverter
    public enum ZonedDateTimeWriteConverter implements Converter<ZonedDateTime, LocalDateTime> {
        INSTANCE;

        @Override
        public LocalDateTime convert(ZonedDateTime zonedDateTime) {
            return zonedDateTime.toLocalDateTime();
        }
    }

    @WritingConverter
    public enum DurationWriteConverter implements Converter<Duration, Long> {
        INSTANCE;

        @Override
        public Long convert(Duration source) {
            return source != null ? source.toMillis() : null;
        }
    }

    @ReadingConverter
    public enum DurationReadConverter implements Converter<Long, Duration> {
        INSTANCE;

        @Override
        public Duration convert(Long source) {
            return source != null ? Duration.ofMillis(source) : null;
        }
    }
}
