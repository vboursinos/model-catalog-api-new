package ai.turintech.modelcatalog.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ai.turintech.modelcatalog.dto.FloatParameterDTO;
import ai.turintech.modelcatalog.dto.ParameterTypeDefinitionDTO;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class FloatParameterFacadeTest extends BasicFacadeTest {
  @Autowired private FloatParameterFacade floatParameterFacade;

  private static final String PARAMETER_TYPE_ID = "323e4567-e89b-12d3-a456-426614174003";
  private static final String EXISTING_FLOAT_PARAMETER_ID = "323e4567-e89b-12d3-a456-426614174007";

  private FloatParameterDTO getFloatParameterDTO() {
    ParameterTypeDefinitionDTO parameterTypeDefinitionDTO = createParameterTypeDefinition();

    FloatParameterDTO floatParameterDTO = new FloatParameterDTO();
    floatParameterDTO.setId(UUID.fromString(EXISTING_FLOAT_PARAMETER_ID));
    floatParameterDTO.setDefaultValue(1.1);
    return floatParameterDTO;
  }

  private ParameterTypeDefinitionDTO createParameterTypeDefinition() {
    ParameterTypeDefinitionDTO parameterTypeDefinitionDTO = new ParameterTypeDefinitionDTO();
    parameterTypeDefinitionDTO.setId(UUID.fromString(PARAMETER_TYPE_ID));
    parameterTypeDefinitionDTO.setOrdering(10);
    return parameterTypeDefinitionDTO;
  }

  @Test
  @Transactional
  void testFindAllFloatParameterFacade() {
    Flux<FloatParameterDTO> floatParametersMono = floatParameterFacade.findAll();
    StepVerifier.create(floatParametersMono.collectList())
        .assertNext(
            floatParameterDTOS ->
                assertEquals(
                    2,
                    floatParameterDTOS.size(),
                    "Returned float parameters do not match expected size"))
        .verifyComplete();
  }

  @Test
  @Transactional
  void testFindByIdFloatParameterFacade() {
    Mono<FloatParameterDTO> floatParameterDTOMono =
        floatParameterFacade.findOne(UUID.fromString(EXISTING_FLOAT_PARAMETER_ID));
    StepVerifier.create(floatParameterDTOMono)
        .assertNext(
            floatParameterDTO -> Assert.assertTrue(floatParameterDTO.getDefaultValue() == 10.1))
        .verifyComplete();
  }

  @Test
  void testExistsByIdFloatParameterFacade() {
    UUID existingFloatParameterId = UUID.fromString(EXISTING_FLOAT_PARAMETER_ID);

    Mono<Boolean> exists = floatParameterFacade.existsById(existingFloatParameterId);

    StepVerifier.create(exists).expectNext(true).verifyComplete();
  }

  @Test
  void testExistsByIdNonExistingFloatParameterFacade() {
    UUID nonExistingFloatParameterId = UUID.randomUUID();

    Mono<Boolean> exists = floatParameterFacade.existsById(nonExistingFloatParameterId);
    StepVerifier.create(exists).expectNext(false).verifyComplete();
  }

  @Test
  @Transactional
  void testDeleteFloatParameterFacade() {
    Mono<FloatParameterDTO> savedFloatParameter = floatParameterFacade.save(getFloatParameterDTO());

    StepVerifier.create(savedFloatParameter)
        .assertNext(
            floatParameterDTO -> {
              StepVerifier.create(floatParameterFacade.delete(floatParameterDTO.getId()))
                  .expectNextCount(0)
                  .verifyComplete();

              StepVerifier.create(floatParameterFacade.findOne(floatParameterDTO.getId()))
                  .expectError(NoSuchElementException.class)
                  .verify();
            })
        .verifyComplete();
  }

  @Test
  @Transactional
  void testFindByIdNonExistingFloatParameterFacade() {
    Mono<FloatParameterDTO> floatParameter = floatParameterFacade.findOne(UUID.randomUUID());

    StepVerifier.create(floatParameter).expectError(NoSuchElementException.class).verify();
  }
}
