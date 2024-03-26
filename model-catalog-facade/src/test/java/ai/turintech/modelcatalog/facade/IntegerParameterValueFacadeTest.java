package ai.turintech.modelcatalog.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ai.turintech.modelcatalog.dto.IntegerParameterValueDTO;
import ai.turintech.modelcatalog.entity.IntegerParameter;
import ai.turintech.modelcatalog.entity.ParameterTypeDefinition;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class IntegerParameterValueFacadeTest extends BasicFacadeTest {

  @Autowired private IntegerParameterValueFacade integerParameterValueFacade;

  private static final String PARAMETER_TYPE_ID = "323e4567-e89b-12d3-a456-426614174001";
  private static final String EXISTING_INTEGER_PARAMETER_VALUE_ID =
      "423e4567-e89b-12d3-a456-426614174004";

  private IntegerParameterValueDTO getIntegerParameterValueDTO() {
    ParameterTypeDefinition parameterTypeDefinition = createParameterTypeDefinition();

    IntegerParameter integerParameter = createIntegerParameter(parameterTypeDefinition);

    IntegerParameterValueDTO integerParameterValueDTO = new IntegerParameterValueDTO();
    integerParameterValueDTO.setLower(1);
    integerParameterValueDTO.setUpper(10);
    return integerParameterValueDTO;
  }

  private IntegerParameterValueDTO getUpdatedIntegerParameterValueDTO() {
    ParameterTypeDefinition parameterTypeDefinition = createParameterTypeDefinition();

    IntegerParameter integerParameter = createIntegerParameter(parameterTypeDefinition);

    IntegerParameterValueDTO integerParameterValueDTO = new IntegerParameterValueDTO();
    integerParameterValueDTO.setId(UUID.fromString(EXISTING_INTEGER_PARAMETER_VALUE_ID));
    integerParameterValueDTO.setLower(1);
    integerParameterValueDTO.setUpper(12);
    return integerParameterValueDTO;
  }

  private IntegerParameter createIntegerParameter(ParameterTypeDefinition parameterTypeDefinition) {
    IntegerParameter integerParameter = new IntegerParameter();
    integerParameter.setId(UUID.fromString(EXISTING_INTEGER_PARAMETER_VALUE_ID));
    integerParameter.setDefaultValue(1);
    return integerParameter;
  }

  private ParameterTypeDefinition createParameterTypeDefinition() {
    ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();
    parameterTypeDefinition.setId(UUID.fromString(PARAMETER_TYPE_ID));
    parameterTypeDefinition.setOrdering(10);
    return parameterTypeDefinition;
  }

  @Test
  void testFindAllIntegerParameterValueFacade() {
    Flux<IntegerParameterValueDTO> integerParameterValues = integerParameterValueFacade.findAll();
    StepVerifier.create(integerParameterValues.collectList())
        .assertNext(
            integerParameterValueDTOS ->
                assertEquals(
                    4,
                    integerParameterValueDTOS.size(),
                    "Returned integer parameter values do not match expected size"))
        .verifyComplete();
  }

  @Test
  void testFindByIdIntegerParameterValueFacade() {
    Mono<IntegerParameterValueDTO> integerParameterValueMonoDTO =
        integerParameterValueFacade.findOne(UUID.fromString(EXISTING_INTEGER_PARAMETER_VALUE_ID));
    StepVerifier.create(integerParameterValueMonoDTO)
        .assertNext(
            integerParameterValueDTO ->
                Assert.assertTrue(integerParameterValueDTO.getLower() == 25))
        .verifyComplete();
  }

  @Test
  void testExistsByIdIntegerParameterValueFacade() {
    Mono<Boolean> exists =
        integerParameterValueFacade.existsById(
            UUID.fromString(EXISTING_INTEGER_PARAMETER_VALUE_ID));
    StepVerifier.create(exists).expectNext(true).verifyComplete();
  }

  @Test
  void testExistsByIdNonExistingIntegerParameterValueFacade() {
    UUID nonExistingIntegerParameterValueId = UUID.randomUUID();

    Mono<Boolean> exists =
        integerParameterValueFacade.existsById(nonExistingIntegerParameterValueId);
    StepVerifier.create(exists).expectNext(false).verifyComplete();
  }

  @Test
  @Transactional
  void testDeleteIntegerParameterValueFacade() {
    Mono<IntegerParameterValueDTO> savedIntegerParameterValue =
        integerParameterValueFacade.save(getIntegerParameterValueDTO());

    StepVerifier.create(savedIntegerParameterValue)
        .assertNext(
            integerParameterValueDTO -> {
              StepVerifier.create(
                      integerParameterValueFacade.delete(integerParameterValueDTO.getId()))
                  .expectNextCount(0)
                  .verifyComplete();

              StepVerifier.create(
                      integerParameterValueFacade.findOne(integerParameterValueDTO.getId()))
                  .expectError(NoSuchElementException.class)
                  .verify();
            })
        .verifyComplete();
  }

  @Test
  @Transactional
  void testFindByIdNonExistingIntegerParameterValueFacade() {
    Mono<IntegerParameterValueDTO> integerParameterValue =
        integerParameterValueFacade.findOne(UUID.randomUUID());

    StepVerifier.create(integerParameterValue).expectError(NoSuchElementException.class).verify();
  }

  @Test
  @Transactional
  void testPartialUpdateIntegerParameterValueFacade() {
    Mono<IntegerParameterValueDTO> updatedIntegerParameterValue =
        integerParameterValueFacade.partialUpdate(getUpdatedIntegerParameterValueDTO());
    updatedIntegerParameterValue.subscribe(
        integerParameterValueDTO -> {
          assertEquals(
              getUpdatedIntegerParameterValueDTO().getUpper(), integerParameterValueDTO.getUpper());
          assertEquals(
              getUpdatedIntegerParameterValueDTO().getId(), integerParameterValueDTO.getId());
        });
  }

  @Test
  @Transactional
  void testUpdateIntegerParameterValueFacade() {
    Mono<IntegerParameterValueDTO> updatedIntegerParameterValue =
        integerParameterValueFacade.update(getUpdatedIntegerParameterValueDTO());
    updatedIntegerParameterValue.subscribe(
        integerParameterValueDTO -> {
          assertEquals(
              getUpdatedIntegerParameterValueDTO().getUpper(), integerParameterValueDTO.getUpper());
          assertEquals(
              getUpdatedIntegerParameterValueDTO().getId(), integerParameterValueDTO.getId());
        });
  }

  @Test
  @Transactional
  void testSaveIntegerParameterValueFacade() {
    Mono<IntegerParameterValueDTO> savedIntegerParameterValue =
        integerParameterValueFacade.save(getIntegerParameterValueDTO());
    savedIntegerParameterValue.subscribe(
        integerParameterValueDTO -> {
          assertEquals(
              getIntegerParameterValueDTO().getUpper(), integerParameterValueDTO.getUpper());
          integerParameterValueFacade.delete(integerParameterValueDTO.getId()).block();
        });
  }
}
