package ai.turintech.modelcatalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ai.turintech.components.architecture.callable.impl.reactive.ReactiveAbstractUUIDIdentityCrudCallableImpl;
import ai.turintech.components.mapper.api.MapperInterface;
import ai.turintech.modelcatalog.dto.FloatParameterRangeDTO;
import ai.turintech.modelcatalog.entity.FloatParameter;
import ai.turintech.modelcatalog.entity.ParameterTypeDefinition;
import ai.turintech.modelcatalog.repository.FloatParameterRangeRepository;
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
public class FloatParameterValueServiceTest {

  private final String EXISTING_FLOAT_PARAMETER_RANGE_ID = "423e4567-e89b-12d3-a456-426614174004";
  private final String NON_EXISTING_FLOAT_PARAMETER_RANGE_ID = UUID.randomUUID().toString();

  @Mock private FloatParameterRangeRepository repository;

  @Mock private MapperInterface<FloatParameterRangeDTO, FloatParameter> mapperInterface;

  @Mock private ApplicationContext context;

  @InjectMocks private FloatParameterRangeServiceImpl floatParameterRangeService;

  private FloatParameterRangeDTO getFloatParameterRangeDTO() {
    ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();
    parameterTypeDefinition.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174001"));
    parameterTypeDefinition.setOrdering(10);

    FloatParameter floatParameter = new FloatParameter();
    floatParameter.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174002"));
    floatParameter.setDefaultValue(1.0);

    FloatParameterRangeDTO floatParameterRangeDTO = new FloatParameterRangeDTO();
    floatParameterRangeDTO.setLower(1.0);
    floatParameterRangeDTO.setUpper(10.0);
    floatParameterRangeDTO.setIsLeftOpen(false);
    floatParameterRangeDTO.setIsRightOpen(false);
    return floatParameterRangeDTO;
  }

  private FloatParameterRangeDTO getUpdatedFloatParameterRangeDTO() {
    ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();
    parameterTypeDefinition.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174001"));
    parameterTypeDefinition.setOrdering(10);

    FloatParameter floatParameter = new FloatParameter();
    floatParameter.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174002"));
    floatParameter.setDefaultValue(1.0);

    FloatParameterRangeDTO floatParameterRangeDTO = new FloatParameterRangeDTO();
    floatParameterRangeDTO.setId(UUID.fromString("423e4567-e89b-12d3-a456-426614174003"));
    floatParameterRangeDTO.setLower(1.0);
    floatParameterRangeDTO.setUpper(10.0);
    floatParameterRangeDTO.setIsLeftOpen(false);
    floatParameterRangeDTO.setIsRightOpen(false);
    return floatParameterRangeDTO;
  }

  private FloatParameterRangeDTO getSavedFloatParameterRangeDTO() {
    ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();
    parameterTypeDefinition.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174001"));
    parameterTypeDefinition.setOrdering(10);

    FloatParameter floatParameter = new FloatParameter();
    floatParameter.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174002"));
    floatParameter.setDefaultValue(1.0);

    FloatParameterRangeDTO floatParameterRangeDTO = new FloatParameterRangeDTO();
    floatParameterRangeDTO.setId(UUID.fromString("423e4567-e89b-12d3-a456-426614174003"));
    floatParameterRangeDTO.setLower(11.0);
    floatParameterRangeDTO.setUpper(100.0);
    floatParameterRangeDTO.setIsLeftOpen(true);
    floatParameterRangeDTO.setIsRightOpen(true);
    return floatParameterRangeDTO;
  }

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    ReflectionTestUtils.setField(floatParameterRangeService, "context", context);
    ReflectionTestUtils.setField(
        floatParameterRangeService, "jdbcScheduler", Schedulers.immediate());
    ReflectionTestUtils.setField(floatParameterRangeService, "repository", repository);
    ReflectionTestUtils.setField(floatParameterRangeService, "mapperInterface", mapperInterface);
  }

  @Test
  void testFindAllFloatParameterRangeService() {
    List<FloatParameterRangeDTO> floatParameterRanges = List.of(getFloatParameterRangeDTO());

    ReactiveAbstractUUIDIdentityCrudCallableImpl<
            List<FloatParameterRangeDTO>, FloatParameterRangeDTO, FloatParameter>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("findAll"),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(floatParameterRanges);

    Mono<List<FloatParameterRangeDTO>> floatParameterRangesMono =
        floatParameterRangeService.findAll();
    StepVerifier.create(floatParameterRangesMono).expectNext(floatParameterRanges).verifyComplete();
  }

  @Test
  void testFindByIdFloatParameterRangeService() {
    UUID existingId = UUID.fromString(EXISTING_FLOAT_PARAMETER_RANGE_ID);
    ReactiveAbstractUUIDIdentityCrudCallableImpl<
            FloatParameterRangeDTO, FloatParameterRangeDTO, FloatParameter>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("findById"),
            eq(existingId),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(getFloatParameterRangeDTO());
    Mono<FloatParameterRangeDTO> floatParameterRangeDTOMono =
        floatParameterRangeService.findOne(existingId);

    StepVerifier.create(floatParameterRangeDTOMono)
        .expectNextMatches(
            floatParameterRangeDTO -> {
              assertEquals(1.0, floatParameterRangeDTO.getLower());
              return true;
            })
        .verifyComplete();
  }

  @Test
  void testExistsByIdFloatParameterRangeServiceForExistingId() {
    UUID existingId = UUID.fromString(EXISTING_FLOAT_PARAMETER_RANGE_ID);

    ReactiveAbstractUUIDIdentityCrudCallableImpl<Boolean, FloatParameterRangeDTO, FloatParameter>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("existsById"),
            eq(existingId),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(true);

    Mono<Boolean> exists = floatParameterRangeService.existsById(existingId);

    StepVerifier.create(exists).expectNext(true).verifyComplete();
  }

  @Test
  void testExistsByIdFloatParameterRangeServiceForNonExistingId() {
    UUID nonExistingId = UUID.fromString(NON_EXISTING_FLOAT_PARAMETER_RANGE_ID);

    ReactiveAbstractUUIDIdentityCrudCallableImpl<Boolean, FloatParameterRangeDTO, FloatParameter>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("existsById"),
            eq(nonExistingId),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(false);

    Mono<Boolean> exists = floatParameterRangeService.existsById(nonExistingId);

    StepVerifier.create(exists).expectNext(false).verifyComplete();
  }

  @Test
  void testPartialUpdateFloatParameterRangeService() {
    ReactiveAbstractUUIDIdentityCrudCallableImpl<
            FloatParameterRangeDTO, FloatParameterRangeDTO, FloatParameter>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("partialUpdate"),
            eq(getUpdatedFloatParameterRangeDTO()),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(getUpdatedFloatParameterRangeDTO());

    Mono<FloatParameterRangeDTO> updatedFloatParameterRange =
        floatParameterRangeService.partialUpdate(getUpdatedFloatParameterRangeDTO());
    StepVerifier.create(updatedFloatParameterRange)
        .expectNextMatches(
            floatParameterRangeDTO -> {
              assertEquals(1.0, floatParameterRangeDTO.getLower());
              assertEquals(
                  UUID.fromString("423e4567-e89b-12d3-a456-426614174003"),
                  floatParameterRangeDTO.getId());
              return true;
            })
        .verifyComplete();
  }

  @Test
  void testUpdateFloatParameterRangeService() {
    ReactiveAbstractUUIDIdentityCrudCallableImpl<
            FloatParameterRangeDTO, FloatParameterRangeDTO, FloatParameter>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("update"),
            eq(getUpdatedFloatParameterRangeDTO()),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(getUpdatedFloatParameterRangeDTO());

    Mono<FloatParameterRangeDTO> updatedFloatParameterRange =
        floatParameterRangeService.update(getUpdatedFloatParameterRangeDTO());
    StepVerifier.create(updatedFloatParameterRange)
        .expectNextMatches(
            floatParameterRangeDTO -> {
              assertEquals(1.0, floatParameterRangeDTO.getLower());
              assertEquals(
                  UUID.fromString("423e4567-e89b-12d3-a456-426614174003"),
                  floatParameterRangeDTO.getId());
              return true;
            })
        .verifyComplete();
  }

  @Test
  void testSaveFloatParameterRangeService() {

    ReactiveAbstractUUIDIdentityCrudCallableImpl<
            FloatParameterRangeDTO, FloatParameterRangeDTO, FloatParameter>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("create"),
            eq(getSavedFloatParameterRangeDTO()),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(getSavedFloatParameterRangeDTO());

    Mono<FloatParameterRangeDTO> savedFloatParameterRange =
        floatParameterRangeService.save(getSavedFloatParameterRangeDTO());
    StepVerifier.create(savedFloatParameterRange)
        .expectNextMatches(
            floatParameterRangeDTO -> {
              assertEquals(
                  getSavedFloatParameterRangeDTO().getLower(), floatParameterRangeDTO.getLower());
              return true;
            })
        .verifyComplete();
  }
}
