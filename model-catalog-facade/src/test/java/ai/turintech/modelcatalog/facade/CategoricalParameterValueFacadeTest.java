package ai.turintech.modelcatalog.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ai.turintech.modelcatalog.dto.CategoricalParameterValueDTO;
import ai.turintech.modelcatalog.entity.*;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class CategoricalParameterValueFacadeTest extends BasicFacadeTest {
  @Autowired private CategoricalParameterValueFacade categoricalParameterValueFacade;

  private static final String PARAMETER_TYPE_ID = "323e4567-e89b-12d3-a456-426614174001";
  private static final String EXISTING_PARAMETER_VALUE_ID = "423e4567-e89b-12d3-a456-426614174003";
  private static final String NON_EXISTING_PARAMETER_VALUE_ID =
      "423e4567-e89b-12d3-a456-426614174005";

  private CategoricalParameterValueDTO getCategoricalParameterValueDTO() {
    ParameterTypeDefinition parameterTypeDefinition = createParameterTypeDefinition();

    CategoricalParameter categoricalParameter = createCategoricalParameter(parameterTypeDefinition);

    CategoricalParameterValueDTO categoricalParameterValueDTO = new CategoricalParameterValueDTO();
    categoricalParameterValueDTO.setValue("test_value");
    return categoricalParameterValueDTO;
  }

  private CategoricalParameterValueDTO getUpdatedCategoricalParameterValueDTO() {
    ParameterTypeDefinition parameterTypeDefinition = createParameterTypeDefinition();

    CategoricalParameter categoricalParameter = createCategoricalParameter(parameterTypeDefinition);

    CategoricalParameterValueDTO categoricalParameterValueDTO = new CategoricalParameterValueDTO();
    categoricalParameterValueDTO.setId(UUID.fromString("423e4567-e89b-12d3-a456-426614174004"));
    categoricalParameterValueDTO.setValue("test_value");
    return categoricalParameterValueDTO;
  }

  private ParameterTypeDefinition createParameterTypeDefinition() {
    ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();
    parameterTypeDefinition.setId(UUID.fromString(PARAMETER_TYPE_ID));
    parameterTypeDefinition.setOrdering(10);
    return parameterTypeDefinition;
  }

  private CategoricalParameter createCategoricalParameter(
      ParameterTypeDefinition parameterTypeDefinition) {
    CategoricalParameter categoricalParameter = new CategoricalParameter();
    categoricalParameter.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174001"));
    categoricalParameter.setDefaultValue("test_defaultValue");
    return categoricalParameter;
  }

  @Test
  void testFindAllCategoricalParameterValueFacade() {
    Flux<CategoricalParameterValueDTO> categoricalParameterValues =
        categoricalParameterValueFacade.findAll();
    StepVerifier.create(categoricalParameterValues.collectList())
        .assertNext(
            categoricalParameterValueDTOS ->
                assertEquals(
                    4,
                    categoricalParameterValueDTOS.size(),
                    "Returned categorical parameter values do not match expected size"))
        .verifyComplete();
  }

  @Test
  void testFindByIdCategoricalParameterValueFacade() {
    Mono<CategoricalParameterValueDTO> categoricalParameterValueDTOMono =
        categoricalParameterValueFacade.findOne(UUID.fromString(EXISTING_PARAMETER_VALUE_ID));

    StepVerifier.create(categoricalParameterValueDTOMono)
        .assertNext(
            categoricalParameterValueDTO ->
                assertEquals("Category3", categoricalParameterValueDTO.getValue()))
        .verifyComplete();
  }

  @Test
  void testExistsByIdCategoricalParameterValueFacade() {
    UUID existingCategoricalParameterValueId = UUID.fromString(EXISTING_PARAMETER_VALUE_ID);

    Mono<Boolean> exists =
        categoricalParameterValueFacade.existsById(existingCategoricalParameterValueId);
    StepVerifier.create(exists).expectNext(true).verifyComplete();
  }

  @Test
  void testExistsByIdNonExistingCategoricalParameterValueFacade() {
    UUID nonExistingCategoricalParameterValueId = UUID.fromString(NON_EXISTING_PARAMETER_VALUE_ID);

    Mono<Boolean> exists =
        categoricalParameterValueFacade.existsById(nonExistingCategoricalParameterValueId);
    StepVerifier.create(exists).expectNext(false).verifyComplete();
  }

  @Test
  @Transactional
  void testDeleteCategoricalParameterValueFacade() {
    Mono<CategoricalParameterValueDTO> savedCategoricalParameterValue =
        categoricalParameterValueFacade.save(getCategoricalParameterValueDTO());

    StepVerifier.create(savedCategoricalParameterValue)
        .assertNext(
            categoricalParameterValueDTO -> {
              StepVerifier.create(
                      categoricalParameterValueFacade.delete(categoricalParameterValueDTO.getId()))
                  .expectNextCount(0)
                  .verifyComplete();

              StepVerifier.create(
                      categoricalParameterValueFacade.findOne(categoricalParameterValueDTO.getId()))
                  .expectError(NoSuchElementException.class)
                  .verify();
            })
        .verifyComplete();
  }

  @Test
  @Transactional
  void testFindByIdNonExistingCategoricalParameterValueFacade() {
    Mono<CategoricalParameterValueDTO> categoricalParameterValue =
        categoricalParameterValueFacade.findOne(UUID.randomUUID());

    StepVerifier.create(categoricalParameterValue)
        .expectError(NoSuchElementException.class)
        .verify();
  }

  @Test
  @Transactional
  void testPartialUpdateCategoricalParameterValueFacade() {
    Mono<CategoricalParameterValueDTO> updatedCategoricalParameterValue =
        categoricalParameterValueFacade.partialUpdate(getUpdatedCategoricalParameterValueDTO());
    updatedCategoricalParameterValue.subscribe(
        categoricalParameterValueDTO -> {
          assertEquals(
              getUpdatedCategoricalParameterValueDTO().getValue(),
              categoricalParameterValueDTO.getValue());
          assertEquals(
              getUpdatedCategoricalParameterValueDTO().getId(),
              categoricalParameterValueDTO.getId());
        });
  }

  @Test
  @Transactional
  void testUpdateCategoricalParameterValueFacade() {
    Mono<CategoricalParameterValueDTO> updatedCategoricalParameterValue =
        categoricalParameterValueFacade.update(getUpdatedCategoricalParameterValueDTO());
    updatedCategoricalParameterValue.subscribe(
        categoricalParameterValueDTO -> {
          assertEquals(
              getUpdatedCategoricalParameterValueDTO().getValue(),
              categoricalParameterValueDTO.getValue());
          assertEquals(
              getUpdatedCategoricalParameterValueDTO().getId(),
              categoricalParameterValueDTO.getId());
        });
  }

  @Test
  @Transactional
  void testSaveCategoricalParameterValueFacade() {
    Mono<CategoricalParameterValueDTO> savedCategoricalParameterValue =
        categoricalParameterValueFacade.save(getCategoricalParameterValueDTO());
    savedCategoricalParameterValue.subscribe(
        categoricalParameterValueDTO -> {
          assertEquals(
              getCategoricalParameterValueDTO().getValue(),
              categoricalParameterValueDTO.getValue());
          categoricalParameterValueFacade.delete(categoricalParameterValueDTO.getId()).block();
        });
  }
}
