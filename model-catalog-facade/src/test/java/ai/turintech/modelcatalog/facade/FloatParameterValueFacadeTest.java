package ai.turintech.modelcatalog.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ai.turintech.modelcatalog.dto.FloatParameterRangeDTO;
import ai.turintech.modelcatalog.entity.*;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class FloatParameterValueFacadeTest extends BasicFacadeTest {
  @Autowired private FloatParameterRangeFacade floatParameterRangeFacade;

  private static final String PARAMETER_TYPE_ID = "323e4567-e89b-12d3-a456-426614174001";
  private static final String EXISTING_FLOAT_PARAMETER_RANGE_ID =
      "423e4567-e89b-12d3-a456-426614174004";

  private FloatParameterRangeDTO getFloatParameterRangeDTO() {
    ParameterTypeDefinition parameterTypeDefinition = createParameterTypeDefinition();

    FloatParameter floatParameter = createFloatParameter();

    FloatParameterRangeDTO floatParameterRangeDTO = new FloatParameterRangeDTO();
    floatParameterRangeDTO.setLower(1.0);
    floatParameterRangeDTO.setUpper(10.0);
    floatParameterRangeDTO.setIsLeftOpen(false);
    floatParameterRangeDTO.setIsRightOpen(false);
    return floatParameterRangeDTO;
  }

  private FloatParameterRangeDTO getUpdatedFloatParameterRangeDTO() {
    ParameterTypeDefinition parameterTypeDefinition = createParameterTypeDefinition();

    FloatParameter floatParameter = createFloatParameter();

    FloatParameterRangeDTO floatParameterRangeDTO = new FloatParameterRangeDTO();
    floatParameterRangeDTO.setId(UUID.fromString(EXISTING_FLOAT_PARAMETER_RANGE_ID));
    floatParameterRangeDTO.setLower(1.0);
    floatParameterRangeDTO.setUpper(10.0);
    floatParameterRangeDTO.setIsLeftOpen(false);
    floatParameterRangeDTO.setIsRightOpen(false);
    return floatParameterRangeDTO;
  }

  private ParameterTypeDefinition createParameterTypeDefinition() {
    ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition();
    parameterTypeDefinition.setId(UUID.fromString(PARAMETER_TYPE_ID));
    parameterTypeDefinition.setOrdering(10);
    return parameterTypeDefinition;
  }

  private FloatParameter createFloatParameter() {
    FloatParameter floatParameter = new FloatParameter();
    floatParameter.setId(UUID.fromString("323e4567-e89b-12d3-a456-426614174002"));
    floatParameter.setDefaultValue(1.0);
    return floatParameter;
  }

  @Test
  void testFindAllFloatParameterRangeFacade() {
    Flux<FloatParameterRangeDTO> floatParameterRangesDTOMono = floatParameterRangeFacade.findAll();
    StepVerifier.create(floatParameterRangesDTOMono.collectList())
        .assertNext(
            floatParameterRangeDTOS ->
                Assertions.assertEquals(
                    4,
                    floatParameterRangeDTOS.size(),
                    "Returned float parameter ranges do not match expected size"))
        .verifyComplete();
  }

  @Test
  void testFindByIdFloatParameterRangeFacade() {
    Mono<FloatParameterRangeDTO> floatParameterRangeDTOMono =
        floatParameterRangeFacade.findOne(UUID.fromString(EXISTING_FLOAT_PARAMETER_RANGE_ID));
    StepVerifier.create(floatParameterRangeDTOMono)
        .assertNext(
            floatParameterRangeDTO -> Assert.assertTrue(floatParameterRangeDTO.getLower() == 25.3))
        .verifyComplete();
  }

  @Test
  void testExistsByIdFloatParameterRangeFacade() {
    UUID existingFloatParameterRangeId = UUID.fromString(EXISTING_FLOAT_PARAMETER_RANGE_ID);

    Mono<Boolean> exists = floatParameterRangeFacade.existsById(existingFloatParameterRangeId);
    StepVerifier.create(exists).expectNext(true).verifyComplete();
  }

  @Test
  void testExistsByIdNonExistingFloatParameterRangeFacade() {
    UUID nonExistingFloatParameterRangeId = UUID.randomUUID();

    Mono<Boolean> exists = floatParameterRangeFacade.existsById(nonExistingFloatParameterRangeId);
    StepVerifier.create(exists).expectNext(false).verifyComplete();
  }

  @Test
  @Transactional
  void testDeleteFloatParameterRangeFacade() {
    Mono<FloatParameterRangeDTO> savedFloatParameterRange =
        floatParameterRangeFacade.save(getFloatParameterRangeDTO());

    StepVerifier.create(savedFloatParameterRange)
        .assertNext(
            floatParameterRangeDTO -> {
              StepVerifier.create(floatParameterRangeFacade.delete(floatParameterRangeDTO.getId()))
                  .expectNextCount(0)
                  .verifyComplete();

              StepVerifier.create(floatParameterRangeFacade.findOne(floatParameterRangeDTO.getId()))
                  .expectError(NoSuchElementException.class)
                  .verify();
            })
        .verifyComplete();
  }

  @Test
  @Transactional
  void testFindByIdNonExistingFloatParameterRangeFacade() {
    Mono<FloatParameterRangeDTO> floatParameterRange =
        floatParameterRangeFacade.findOne(UUID.randomUUID());

    StepVerifier.create(floatParameterRange).expectError(NoSuchElementException.class).verify();
  }

  @Test
  @Transactional
  void testPartialUpdateFloatParameterRangeFacade() {
    Mono<FloatParameterRangeDTO> updatedFloatParameterRange =
        floatParameterRangeFacade.partialUpdate(getUpdatedFloatParameterRangeDTO());
    updatedFloatParameterRange.subscribe(
        floatParameterRangeDTO -> {
          assertEquals(
              getUpdatedFloatParameterRangeDTO().getLower(), floatParameterRangeDTO.getLower());
          assertEquals(getUpdatedFloatParameterRangeDTO().getId(), floatParameterRangeDTO.getId());
        });
  }

  @Test
  @Transactional
  void testUpdateFloatParameterRangeFacade() {
    Mono<FloatParameterRangeDTO> updatedFloatParameterRange =
        floatParameterRangeFacade.update(getUpdatedFloatParameterRangeDTO());
    updatedFloatParameterRange.subscribe(
        floatParameterRangeDTO -> {
          assertEquals(
              getUpdatedFloatParameterRangeDTO().getLower(), floatParameterRangeDTO.getLower());
          assertEquals(getUpdatedFloatParameterRangeDTO().getId(), floatParameterRangeDTO.getId());
        });
  }

  @Test
  @Transactional
  void testSaveFloatParameterRangeFacade() {
    Mono<FloatParameterRangeDTO> savedFloatParameterRange =
        floatParameterRangeFacade.save(getFloatParameterRangeDTO());
    savedFloatParameterRange.subscribe(
        floatParameterRangeDTO -> {
          assertEquals(getFloatParameterRangeDTO().getLower(), floatParameterRangeDTO.getLower());
          floatParameterRangeFacade.delete(floatParameterRangeDTO.getId()).block();
        });
  }
}
