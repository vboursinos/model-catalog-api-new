package ai.turintech.modelcatalog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ai.turintech.components.architecture.callable.impl.reactive.ReactiveAbstractUUIDIdentityCrudCallableImpl;
import ai.turintech.components.mapper.api.MapperInterface;
import ai.turintech.modelcatalog.dto.CategoricalParameterValueDTO;
import ai.turintech.modelcatalog.entity.CategoricalParameter;
import ai.turintech.modelcatalog.entity.CategoricalParameterValue;
import ai.turintech.modelcatalog.entity.ParameterTypeDefinition;
import ai.turintech.modelcatalog.repository.CategoricalParameterValueRepository;
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
public class CategoricalParameterValueServiceTest {

  private final String EXISTING_CATEGORICAL_PARAMETER_VALUE_ID =
      "423e4567-e89b-12d3-a456-426614174003";
  private final String NON_EXISTING_CATEGORICAL_PARAMETER_VALUE_ID = UUID.randomUUID().toString();

  @Mock private CategoricalParameterValueRepository repository;

  @Mock
  private MapperInterface<CategoricalParameterValueDTO, CategoricalParameterValue> mapperInterface;

  @Mock private ApplicationContext context;

  @InjectMocks private CategoricalParameterValueServiceImpl categoricalParameterValueService;

  private CategoricalParameterValueDTO getCategoricalParameterValueDTO() {
    ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();
    parameterTypeDefinition.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174001"));
    parameterTypeDefinition.setOrdering(10);

    CategoricalParameter categoricalParameter = new CategoricalParameter();
    categoricalParameter.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174001"));
    categoricalParameter.setDefaultValue("test_defaultValue");

    CategoricalParameterValueDTO categoricalParameterValueDTO = new CategoricalParameterValueDTO();
    categoricalParameterValueDTO.setValue("test_value");
    return categoricalParameterValueDTO;
  }

  private CategoricalParameterValueDTO getUpdatedCategoricalParameterValueDTO() {
    ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();
    parameterTypeDefinition.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174001"));
    parameterTypeDefinition.setOrdering(10);

    CategoricalParameter categoricalParameter = new CategoricalParameter();
    categoricalParameter.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174001"));
    categoricalParameter.setDefaultValue("test_defaultValue");

    CategoricalParameterValueDTO categoricalParameterValueDTO = new CategoricalParameterValueDTO();
    categoricalParameterValueDTO.setId(UUID.fromString("423e4567-e89b-12d3-a456-426614174004"));
    categoricalParameterValueDTO.setValue("test_value_updated");
    return categoricalParameterValueDTO;
  }

  private CategoricalParameterValueDTO getSavedCategoricalParameterValueDTO() {
    ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();
    parameterTypeDefinition.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174001"));
    parameterTypeDefinition.setOrdering(10);

    CategoricalParameter categoricalParameter = new CategoricalParameter();
    categoricalParameter.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174001"));
    categoricalParameter.setDefaultValue("test_defaultValue");

    CategoricalParameterValueDTO categoricalParameterValueDTO = new CategoricalParameterValueDTO();
    categoricalParameterValueDTO.setId(UUID.fromString("423e4567-e89b-12d3-a456-426614174004"));
    categoricalParameterValueDTO.setValue("test_value");
    return categoricalParameterValueDTO;
  }

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    ReflectionTestUtils.setField(categoricalParameterValueService, "context", context);
    ReflectionTestUtils.setField(
        categoricalParameterValueService, "jdbcScheduler", Schedulers.immediate());
    ReflectionTestUtils.setField(categoricalParameterValueService, "repository", repository);
    ReflectionTestUtils.setField(
        categoricalParameterValueService, "mapperInterface", mapperInterface);
  }

  @Test
  void testFindAllCategoricalParameterValueService() {
    List<CategoricalParameterValueDTO> categoricalParameterValues =
        List.of(getCategoricalParameterValueDTO());

    ReactiveAbstractUUIDIdentityCrudCallableImpl<
            List<CategoricalParameterValueDTO>,
            CategoricalParameterValueDTO,
            CategoricalParameterValue>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("findAll"),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(categoricalParameterValues);

    Mono<List<CategoricalParameterValueDTO>> categoricalParameterValuesMono =
        categoricalParameterValueService.findAll();
    StepVerifier.create(categoricalParameterValuesMono)
        .expectNext(categoricalParameterValues)
        .verifyComplete();
  }

  @Test
  void testFindByIdCategoricalParameterValueRepository() {
    UUID existingId = UUID.fromString(EXISTING_CATEGORICAL_PARAMETER_VALUE_ID);
    ReactiveAbstractUUIDIdentityCrudCallableImpl<
            CategoricalParameterValueDTO, CategoricalParameterValueDTO, CategoricalParameterValue>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("findById"),
            eq(existingId),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(getCategoricalParameterValueDTO());
    Mono<CategoricalParameterValueDTO> categoricalParameterValueDTOMono =
        categoricalParameterValueService.findOne(existingId);

    StepVerifier.create(categoricalParameterValueDTOMono)
        .expectNextMatches(
            categoricalParameterValueDTO -> {
              assertEquals("test_value", categoricalParameterValueDTO.getValue());
              return true;
            })
        .verifyComplete();
  }

  @Test
  void testExistsByIdCategoricalParameterValueServiceForExistingId() {
    UUID existingId = UUID.fromString(EXISTING_CATEGORICAL_PARAMETER_VALUE_ID);

    ReactiveAbstractUUIDIdentityCrudCallableImpl<
            Boolean, CategoricalParameterValueDTO, CategoricalParameterValue>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("existsById"),
            eq(existingId),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(true);

    Mono<Boolean> exists = categoricalParameterValueService.existsById(existingId);

    StepVerifier.create(exists).expectNext(true).verifyComplete();
  }

  @Test
  void testExistsByIdCategoricalParameterValueServiceForNonExistingId() {
    UUID nonExistingId = UUID.fromString(NON_EXISTING_CATEGORICAL_PARAMETER_VALUE_ID);

    ReactiveAbstractUUIDIdentityCrudCallableImpl<
            Boolean, CategoricalParameterValueDTO, CategoricalParameterValue>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("existsById"),
            eq(nonExistingId),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(false);

    Mono<Boolean> exists = categoricalParameterValueService.existsById(nonExistingId);

    StepVerifier.create(exists).expectNext(false).verifyComplete();
  }

  @Test
  void testPartialUpdateCategoricalParameterValueService() {
    ReactiveAbstractUUIDIdentityCrudCallableImpl<
            CategoricalParameterValueDTO, CategoricalParameterValueDTO, CategoricalParameterValue>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("partialUpdate"),
            eq(getUpdatedCategoricalParameterValueDTO()),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(getUpdatedCategoricalParameterValueDTO());

    Mono<CategoricalParameterValueDTO> updatedCategoricalParameterValue =
        categoricalParameterValueService.partialUpdate(getUpdatedCategoricalParameterValueDTO());
    StepVerifier.create(updatedCategoricalParameterValue)
        .expectNextMatches(
            categoricalParameterValueDTO -> {
              assertEquals("test_value_updated", categoricalParameterValueDTO.getValue());
              assertEquals(
                  UUID.fromString("423e4567-e89b-12d3-a456-426614174004"),
                  categoricalParameterValueDTO.getId());
              return true;
            })
        .verifyComplete();
  }

  @Test
  void testUpdateCategoricalParameterValueService() {
    ReactiveAbstractUUIDIdentityCrudCallableImpl<
            CategoricalParameterValueDTO, CategoricalParameterValueDTO, CategoricalParameterValue>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("update"),
            eq(getUpdatedCategoricalParameterValueDTO()),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(getUpdatedCategoricalParameterValueDTO());

    Mono<CategoricalParameterValueDTO> updatedCategoricalParameterValue =
        categoricalParameterValueService.update(getUpdatedCategoricalParameterValueDTO());
    StepVerifier.create(updatedCategoricalParameterValue)
        .expectNextMatches(
            categoricalParameterValueDTO -> {
              assertEquals(
                  getUpdatedCategoricalParameterValueDTO().getValue(),
                  categoricalParameterValueDTO.getValue());
              assertEquals(
                  UUID.fromString("423e4567-e89b-12d3-a456-426614174004"),
                  categoricalParameterValueDTO.getId());
              return true;
            })
        .verifyComplete();
  }

  @Test
  void testSaveCategoricalParameterValueService() {

    ReactiveAbstractUUIDIdentityCrudCallableImpl<
            CategoricalParameterValueDTO, CategoricalParameterValueDTO, CategoricalParameterValue>
        callable = mock(ReactiveAbstractUUIDIdentityCrudCallableImpl.class);

    when(context.getBean(
            eq(ReactiveAbstractUUIDIdentityCrudCallableImpl.class),
            eq("create"),
            eq(getSavedCategoricalParameterValueDTO()),
            eq(repository),
            eq(mapperInterface)))
        .thenReturn(callable);

    when(callable.call()).thenReturn(getSavedCategoricalParameterValueDTO());

    Mono<CategoricalParameterValueDTO> savedCategoricalParameterValue =
        categoricalParameterValueService.save(getSavedCategoricalParameterValueDTO());
    StepVerifier.create(savedCategoricalParameterValue)
        .expectNextMatches(
            categoricalParameterValueDTO -> {
              assertEquals(
                  getSavedCategoricalParameterValueDTO().getValue(),
                  categoricalParameterValueDTO.getValue());
              return true;
            })
        .verifyComplete();
  }
}
