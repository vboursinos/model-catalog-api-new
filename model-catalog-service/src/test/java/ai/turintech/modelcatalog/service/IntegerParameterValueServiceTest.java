package ai.turintech.modelcatalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ai.turintech.components.architecture.callable.impl.reactive.ReactiveAbstractUUIDIdentityCrudCallableImpl;
import ai.turintech.components.mapper.api.MapperInterface;
import ai.turintech.modelcatalog.dto.IntegerParameterValueDTO;
import ai.turintech.modelcatalog.entity.IntegerParameter;
import ai.turintech.modelcatalog.entity.ParameterTypeDefinition;
import ai.turintech.modelcatalog.repository.IntegerParameterValueRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class IntegerParameterValueServiceTest {

  private final String EXISTING_INTEGER_PARAMETER_VALUE_ID = "423e4567-e89b-12d3-a456-426614174004";
  private final String NON_EXISTING_INTEGER_PARAMETER_VALUE_ID =
      "123e4567-e89b-12d3-a456-426614174099";

  @Mock private IntegerParameterValueRepository repository;

  @Mock private MapperInterface<IntegerParameterValueDTO, IntegerParameter> mapperInterface;

  @Mock private ApplicationContext context;

  @InjectMocks private IntegerParameterValueServiceImpl integerParameterValueService;

  private IntegerParameterValueDTO getIntegerParameterValueDTO() {
    ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();
    parameterTypeDefinition.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174001"));
    parameterTypeDefinition.setOrdering(10);

    IntegerParameter integerParameter = new IntegerParameter();
    integerParameter.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174001"));
    integerParameter.setDefaultValue(1);

    IntegerParameterValueDTO integerParameterValueDTO = new IntegerParameterValueDTO();
    integerParameterValueDTO.setLower(1);
    integerParameterValueDTO.setUpper(10);
    return integerParameterValueDTO;
  }

  private IntegerParameterValueDTO getUpdatedIntegerParameterValueDTO() {
    ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();
    parameterTypeDefinition.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174001"));
    parameterTypeDefinition.setOrdering(10);

    IntegerParameter integerParameter = new IntegerParameter();
    integerParameter.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174001"));
    integerParameter.setDefaultValue(1);

    IntegerParameterValueDTO integerParameterValueDTO = new IntegerParameterValueDTO();
    integerParameterValueDTO.setId(UUID.fromString(EXISTING_INTEGER_PARAMETER_VALUE_ID));
    integerParameterValueDTO.setLower(1);
    integerParameterValueDTO.setUpper(10);
    return integerParameterValueDTO;
  }

  private IntegerParameterValueDTO getSavedIntegerParameterValueDTO() {
    ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();
    parameterTypeDefinition.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174001"));
    parameterTypeDefinition.setOrdering(10);

    IntegerParameter integerParameter = new IntegerParameter();
    integerParameter.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174001"));
    integerParameter.setDefaultValue(1);

    IntegerParameterValueDTO integerParameterValueDTO = new IntegerParameterValueDTO();
    integerParameterValueDTO.setId(UUID.fromString(EXISTING_INTEGER_PARAMETER_VALUE_ID));
    integerParameterValueDTO.setLower(10);
    integerParameterValueDTO.setUpper(100);
    return integerParameterValueDTO;
  }

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    ReflectionTestUtils.setField(integerParameterValueService, "context", context);
    ReflectionTestUtils.setField(
        integerParameterValueService, "jdbcScheduler", Schedulers.immediate());
    ReflectionTestUtils.setField(integerParameterValueService, "repository", repository);
    ReflectionTestUtils.setField(integerParameterValueService, "mapperInterface", mapperInterface);
  }

  @Test
  void testFindAllIntegerParameterValueService() {
    List<IntegerParameterValueDTO> integerParameterValues = List.of(getIntegerParameterValueDTO());

    ReactiveAbstractUUIDIdentityCrudCallableImpl<
            List<IntegerParameterValueDTO>, IntegerParameterValueDTO, IntegerParameter>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("findAll"),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(integerParameterValues);

    Mono<List<IntegerParameterValueDTO>> integerParameterValuesMono =
        integerParameterValueService.findAll();
    StepVerifier.create(integerParameterValuesMono)
        .expectNext(integerParameterValues)
        .verifyComplete();
  }

  @Test
  void testFindByIdIntegerParameterValueService() {
    UUID existingId = UUID.fromString(EXISTING_INTEGER_PARAMETER_VALUE_ID);
    ReactiveAbstractUUIDIdentityCrudCallableImpl<
            IntegerParameterValueDTO, IntegerParameterValueDTO, IntegerParameter>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("findById"),
            eq(existingId),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(getIntegerParameterValueDTO());
    Mono<IntegerParameterValueDTO> integerParameterValueDTOMono =
        integerParameterValueService.findOne(existingId);

    StepVerifier.create(integerParameterValueDTOMono)
        .expectNextMatches(
            integerParameterValueDTO -> {
              assertEquals(1, integerParameterValueDTO.getLower());
              return true;
            })
        .verifyComplete();
  }

  @Test
  void testExistsByIdWithExistingId() {
    UUID existingId = UUID.fromString(EXISTING_INTEGER_PARAMETER_VALUE_ID);

    ReactiveAbstractUUIDIdentityCrudCallableImpl<
            Boolean, IntegerParameterValueDTO, IntegerParameter>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("existsById"),
            eq(existingId),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(true);

    Mono<Boolean> exists = integerParameterValueService.existsById(existingId);

    StepVerifier.create(exists).expectNext(true).verifyComplete();
  }

  @Test
  void testExistsByIdWithNonExistingId() {
    UUID nonExistingId = UUID.fromString(NON_EXISTING_INTEGER_PARAMETER_VALUE_ID);

    ReactiveAbstractUUIDIdentityCrudCallableImpl<
            Boolean, IntegerParameterValueDTO, IntegerParameter>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("existsById"),
            eq(nonExistingId),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(false);

    Mono<Boolean> exists = integerParameterValueService.existsById(nonExistingId);

    StepVerifier.create(exists).expectNext(false).verifyComplete();
  }

  @Test
  @Transactional
  void testPartialUpdateIntegerParameterValueService() {
    ReactiveAbstractUUIDIdentityCrudCallableImpl<
            IntegerParameterValueDTO, IntegerParameterValueDTO, IntegerParameter>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("partialUpdate"),
            eq(getUpdatedIntegerParameterValueDTO()),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(getUpdatedIntegerParameterValueDTO());

    Mono<IntegerParameterValueDTO> updatedIntegerParameterValue =
        integerParameterValueService.partialUpdate(getUpdatedIntegerParameterValueDTO());
    StepVerifier.create(updatedIntegerParameterValue)
        .expectNextMatches(
            integerParameterValueDTO -> {
              assertEquals(10, integerParameterValueDTO.getUpper());
              assertEquals(
                  UUID.fromString("423e4567-e89b-12d3-a456-426614174004"),
                  integerParameterValueDTO.getId());
              return true;
            })
        .verifyComplete();
  }

  @Test
  @Transactional
  void testUpdateIntegerParameterValueService() {
    ReactiveAbstractUUIDIdentityCrudCallableImpl<
            IntegerParameterValueDTO, IntegerParameterValueDTO, IntegerParameter>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("update"),
            eq(getUpdatedIntegerParameterValueDTO()),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(getUpdatedIntegerParameterValueDTO());

    Mono<IntegerParameterValueDTO> updatedIntegerParameterValue =
        integerParameterValueService.update(getUpdatedIntegerParameterValueDTO());
    StepVerifier.create(updatedIntegerParameterValue)
        .expectNextMatches(
            integerParameterValueDTO -> {
              assertEquals(10, integerParameterValueDTO.getUpper());
              assertEquals(
                  UUID.fromString("423e4567-e89b-12d3-a456-426614174004"),
                  integerParameterValueDTO.getId());
              return true;
            })
        .verifyComplete();
  }

  @Test
  @Transactional
  void testSaveIntegerParameterValueService() {

    ReactiveAbstractUUIDIdentityCrudCallableImpl<
            IntegerParameterValueDTO, IntegerParameterValueDTO, IntegerParameter>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("create"),
            eq(getSavedIntegerParameterValueDTO()),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(getSavedIntegerParameterValueDTO());

    Mono<IntegerParameterValueDTO> savedIntegerParameterValue =
        integerParameterValueService.save(getSavedIntegerParameterValueDTO());
    StepVerifier.create(savedIntegerParameterValue)
        .expectNextMatches(
            integerParameterValueDTO -> {
              assertEquals(
                  getSavedIntegerParameterValueDTO().getUpper(),
                  integerParameterValueDTO.getUpper());
              return true;
            })
        .verifyComplete();
  }
}
