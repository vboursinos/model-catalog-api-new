package ai.turintech.modelcatalog.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ai.turintech.modelcatalog.dto.ParameterDistributionTypeDTO;
import ai.turintech.modelcatalog.dto.ParameterTypeDTO;
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
public class ParameterTypeDefinitionFacadeTest extends BasicFacadeTest {
  private final String EXISTING_PARAMETER_TYPE_DEFINITION_ID =
      "323e4567-e89b-12d3-a456-426614174008";
  private final String NON_EXISTING_PARAMETER_TYPE_DEFINITION_ID = UUID.randomUUID().toString();

  private final String EXISTING_PARAMETER_TYPE_ID = "1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27";
  private final String EXISTING_PARAMETER_DISTRIBUTION_TYPE_ID =
      "1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27";

  @Autowired private ParameterTypeDefinitionFacade parameterTypeDefinitionFacade;

  private ParameterTypeDefinitionDTO getParameterTypeDefinitionDTO() {

    ParameterDistributionTypeDTO parameterDistributionTypeDTO = new ParameterDistributionTypeDTO();
    parameterDistributionTypeDTO.setId(UUID.fromString(EXISTING_PARAMETER_DISTRIBUTION_TYPE_ID));
    parameterDistributionTypeDTO.setName("parameterdistributiontype1");

    ParameterTypeDTO parameterTypeDTO = new ParameterTypeDTO();
    parameterTypeDTO.setId(UUID.fromString(EXISTING_PARAMETER_TYPE_ID));
    parameterTypeDTO.setName("parametertype1");

    ParameterTypeDefinitionDTO parameterTypeDefinitionDTO = new ParameterTypeDefinitionDTO();
    parameterTypeDefinitionDTO.setDistribution(parameterDistributionTypeDTO);
    parameterTypeDefinitionDTO.setType(parameterTypeDTO);
    parameterTypeDefinitionDTO.setOrdering(10);

    return parameterTypeDefinitionDTO;
  }

  private ParameterTypeDefinitionDTO getUpdatedParameterTypeDefinitionDTO() {

    ParameterDistributionTypeDTO parameterDistributionTypeDTO = new ParameterDistributionTypeDTO();
    parameterDistributionTypeDTO.setId(UUID.fromString("2b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d28"));
    parameterDistributionTypeDTO.setName("parameterdistributiontype2");

    ParameterTypeDTO parameterTypeDTO = new ParameterTypeDTO();
    parameterTypeDTO.setId(UUID.fromString("4b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d21"));
    parameterTypeDTO.setName("float");

    ParameterTypeDefinitionDTO parameterTypeDefinitionDTO = new ParameterTypeDefinitionDTO();
    parameterTypeDefinitionDTO.setId(UUID.fromString(EXISTING_PARAMETER_TYPE_DEFINITION_ID));
    parameterTypeDefinitionDTO.setDistribution(parameterDistributionTypeDTO);
    parameterTypeDefinitionDTO.setType(parameterTypeDTO);
    parameterTypeDefinitionDTO.setOrdering(3);

    return parameterTypeDefinitionDTO;
  }

  @Test
  @Transactional
  void testFindAllParameterTypeDefinitionFacade() {
    Flux<ParameterTypeDefinitionDTO> parameterTypeDefinitionDTOsMono =
        parameterTypeDefinitionFacade.findAll();

    parameterTypeDefinitionDTOsMono
        .collectList()
        .blockOptional()
        .ifPresent(
            parameterTypeDefinitionDTOS -> {
              assertEquals(
                  8,
                  parameterTypeDefinitionDTOS.size(),
                  "Returned parameter type definitions do not match expected size");
            });
  }

  @Test
  @Transactional
  void testFindByIdParameterTypeDefinitionFacade() {
    Mono<ParameterTypeDefinitionDTO> parameterTypeDefinitionDTOMono =
        parameterTypeDefinitionFacade.findOne(
            UUID.fromString(EXISTING_PARAMETER_TYPE_DEFINITION_ID));

    parameterTypeDefinitionDTOMono.subscribe(
        parameterTypeDefinitionDTO -> {
          Assert.assertTrue(1 == parameterTypeDefinitionDTO.getOrdering());
        });
  }

  @Test
  @Transactional
  void testExistsByIdParameterTypeDefinitionFacade() {
    UUID existingParameterTypeDefinitionId = UUID.fromString(EXISTING_PARAMETER_TYPE_DEFINITION_ID);

    Mono<Boolean> exists =
        parameterTypeDefinitionFacade.existsById(existingParameterTypeDefinitionId);

    StepVerifier.create(exists).expectNext(true).verifyComplete();
  }

  @Test
  @Transactional
  void testExistsByIdNonExistingParameterTypeDefinitionFacade() {
    Mono<Boolean> exists =
        parameterTypeDefinitionFacade.existsById(
            UUID.fromString(NON_EXISTING_PARAMETER_TYPE_DEFINITION_ID));
    StepVerifier.create(exists).expectNext(false).verifyComplete();
  }

  @Test
  @Transactional
  void testSaveParameterTypeDefinitionFacade() {
    Mono<ParameterTypeDefinitionDTO> savedParameterTypeDefinitionDTO =
        parameterTypeDefinitionFacade.save(getParameterTypeDefinitionDTO());
    savedParameterTypeDefinitionDTO.subscribe(
        parameterTypeDefinitionDTO -> {
          Assert.assertEquals(
              getParameterTypeDefinitionDTO().getOrdering(),
              parameterTypeDefinitionDTO.getOrdering());
          parameterTypeDefinitionFacade.delete(parameterTypeDefinitionDTO.getId()).block();
        });
  }

//  @Test
//  @Transactional
//  void testUpdateParameterTypeDefinitionFacade() {
//    Mono<ParameterTypeDefinitionDTO> updateParameterTypeDefinitionDTO =
//        parameterTypeDefinitionFacade.update(getUpdatedParameterTypeDefinitionDTO());
//    updateParameterTypeDefinitionDTO.subscribe(
//        parameterTypeDefinitionDTO -> {
//          Assert.assertEquals(
//              getUpdatedParameterTypeDefinitionDTO().getOrdering(),
//              parameterTypeDefinitionDTO.getOrdering());
//        });
//  }

  @Test
  @Transactional
  void testPartialUpdateParameterTypeDefinitionFacade() {
    Mono<ParameterTypeDefinitionDTO> updateParameterTypeDefinitionDTO =
        parameterTypeDefinitionFacade.partialUpdate(getUpdatedParameterTypeDefinitionDTO());
    updateParameterTypeDefinitionDTO.subscribe(
        parameterTypeDefinitionDTO -> {
          Assert.assertEquals(
              getUpdatedParameterTypeDefinitionDTO().getOrdering(),
              parameterTypeDefinitionDTO.getOrdering());
        });
  }

  @Test
  @Transactional
  void testDeleteParameterTypeDefinitionFacade() {
    Mono<ParameterTypeDefinitionDTO> savedParameterTypeDefinition =
        parameterTypeDefinitionFacade.save(getParameterTypeDefinitionDTO());

    savedParameterTypeDefinition.subscribe(
        parameterTypeDefinitionDTO -> {
          Mono<Void> deleteResult =
              parameterTypeDefinitionFacade.delete(parameterTypeDefinitionDTO.getId());
          deleteResult.subscribe(
              result -> {
                Assert.assertNull(result);
                Mono<ParameterTypeDefinitionDTO> findResult =
                    parameterTypeDefinitionFacade.findOne(parameterTypeDefinitionDTO.getId());
                findResult.subscribe(
                    notFoundParameterTypeDefinitionDTO ->
                        Assert.assertNull(notFoundParameterTypeDefinitionDTO),
                    throwable -> Assert.assertTrue(throwable instanceof NoSuchElementException));
              });
        });
  }

  @Test
  @Transactional
  void testFindByIdNonExistingParameterTypeDefinitionFacade() {
    Mono<ParameterTypeDefinitionDTO> parameterTypeDefinition =
        parameterTypeDefinitionFacade.findOne(
            UUID.fromString(NON_EXISTING_PARAMETER_TYPE_DEFINITION_ID));

    StepVerifier.create(parameterTypeDefinition).expectError(NoSuchElementException.class).verify();
  }

  @Test
  @Transactional
  void testSaveAndUpdateParameterTypeDefinitionFacade() {
    Mono<ParameterTypeDefinitionDTO> savedParameterTypeDefinition =
        parameterTypeDefinitionFacade.save(getParameterTypeDefinitionDTO());

    savedParameterTypeDefinition.subscribe(
        parameterTypeDefinitionDTO -> {
          ParameterTypeDefinitionDTO updatedParameterTypeDefinitionDTO =
              getUpdatedParameterTypeDefinitionDTO();
          updatedParameterTypeDefinitionDTO.setId(parameterTypeDefinitionDTO.getId());
          Mono<ParameterTypeDefinitionDTO> updatedParameterTypeDefinitionMono =
              parameterTypeDefinitionFacade.save(updatedParameterTypeDefinitionDTO);

          updatedParameterTypeDefinitionMono.subscribe(
              updatedParameterTypeDefinition -> {
                Assert.assertEquals(
                    updatedParameterTypeDefinition.getOrdering(),
                    updatedParameterTypeDefinitionDTO.getOrdering());
                parameterTypeDefinitionFacade
                    .delete(updatedParameterTypeDefinition.getId())
                    .block();
              });
        });
  }
}
