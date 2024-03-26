package ai.turintech.modelcatalog.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ai.turintech.modelcatalog.dto.IntegerParameterDTO;
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
public class IntegerParameterFacadeTest extends BasicFacadeTest {

  @Autowired private IntegerParameterFacade integerParameterFacade;

  private static final String PARAMETER_TYPE_ID = "323e4567-e89b-12d3-a456-426614174003";
  private static final String EXISTING_INTEGER_PARAMETER_ID =
      "323e4567-e89b-12d3-a456-426614174005";

  private IntegerParameterDTO getIntegerParameterDTO() {
    ParameterTypeDefinitionDTO parameterTypeDefinitionDTO = createParameterTypeDefinition();

    IntegerParameterDTO integerParameterDTO = new IntegerParameterDTO();
    integerParameterDTO.setId(UUID.fromString(EXISTING_INTEGER_PARAMETER_ID));
    integerParameterDTO.setDefaultValue(1);
    return integerParameterDTO;
  }

  private ParameterTypeDefinitionDTO createParameterTypeDefinition() {
    ParameterTypeDefinitionDTO parameterTypeDefinitionDTO = new ParameterTypeDefinitionDTO();
    parameterTypeDefinitionDTO.setId(UUID.fromString(PARAMETER_TYPE_ID));
    parameterTypeDefinitionDTO.setOrdering(10);
    return parameterTypeDefinitionDTO;
  }

  @Test
  @Transactional
  void testFindAllIntegerParameterFacade() {
    Flux<IntegerParameterDTO> integerParametersMono = integerParameterFacade.findAll();
    StepVerifier.create(integerParametersMono.collectList())
        .assertNext(
            integerParameterDTOS ->
                assertEquals(
                    2,
                    integerParameterDTOS.size(),
                    "Returned integer parameters do not match expected size"))
        .verifyComplete();
  }

  @Test
  @Transactional
  void testFindByIdIntegerParameterFacade() {
    Mono<IntegerParameterDTO> integerParameterDTOMono =
        integerParameterFacade.findOne(UUID.fromString(EXISTING_INTEGER_PARAMETER_ID));
    StepVerifier.create(integerParameterDTOMono)
        .assertNext(
            integerParameterDTO -> Assert.assertTrue(integerParameterDTO.getDefaultValue() == 10))
        .verifyComplete();
  }

  @Test
  void testExistsByIdIntegerParameterFacade() {
    Mono<Boolean> exists =
        integerParameterFacade.existsById(UUID.fromString(EXISTING_INTEGER_PARAMETER_ID));
    StepVerifier.create(exists).expectNext(true).verifyComplete();
  }

  @Test
  void testExistsByIdNonExistingIntegerParameterFacade() {
    UUID nonExistingIntegerParameterId = UUID.randomUUID();

    Mono<Boolean> exists = integerParameterFacade.existsById(nonExistingIntegerParameterId);
    StepVerifier.create(exists).expectNext(false).verifyComplete();
  }

  @Test
  @Transactional
  void testDeleteIntegerParameterFacade() {
    Mono<IntegerParameterDTO> savedIntegerParameter =
        integerParameterFacade.save(getIntegerParameterDTO());

    StepVerifier.create(savedIntegerParameter)
        .assertNext(
            integerParameterDTO -> {
              StepVerifier.create(integerParameterFacade.delete(integerParameterDTO.getId()))
                  .expectNextCount(0)
                  .verifyComplete();

              StepVerifier.create(integerParameterFacade.findOne(integerParameterDTO.getId()))
                  .expectError(NoSuchElementException.class)
                  .verify();
            })
        .verifyComplete();
  }

  @Test
  @Transactional
  void testFindByIdNonExistingIntegerParameterFacade() {
    Mono<IntegerParameterDTO> integerParameter = integerParameterFacade.findOne(UUID.randomUUID());

    StepVerifier.create(integerParameter).expectError(NoSuchElementException.class).verify();
  }
}
