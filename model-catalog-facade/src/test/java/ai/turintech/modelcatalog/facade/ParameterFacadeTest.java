package ai.turintech.modelcatalog.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ai.turintech.modelcatalog.dto.ParameterDTO;
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
public class ParameterFacadeTest extends BasicFacadeTest {
  private final String EXISTING_PARAMETER_ID = "523e4567-e89b-12d3-a456-426614174001";
  private final String EXISTING_PARAMETER_ID_FOR_UPDATE = "4b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d21";
  private final String NON_EXISTING_PARAMETER_ID = UUID.randomUUID().toString();

  @Autowired private ParameterFacade parameterFacade;

  private ParameterDTO getParameterDTO() {
    ParameterDTO parameter = new ParameterDTO();
    parameter.setName("test_parameter");
    parameter.setDescription("test_description");
    parameter.setEnabled(true);
    parameter.setFixedValue(true);
    parameter.setLabel("test_label");
    parameter.setOrdering(1);
    return parameter;
  }

  private ParameterDTO getUpdatedParameterDTO() {
    ParameterDTO parameterDTO = new ParameterDTO();
    parameterDTO.setId(UUID.fromString(EXISTING_PARAMETER_ID_FOR_UPDATE));
    parameterDTO.setName("update_parameter");
    parameterDTO.setDescription("test_description");
    parameterDTO.setEnabled(true);
    parameterDTO.setFixedValue(true);
    parameterDTO.setLabel("test_label");
    parameterDTO.setOrdering(1);
    return parameterDTO;
  }

  private ParameterDTO getPartialUpdatedParameterDTO() {
    ParameterDTO parameterDTO = new ParameterDTO();
    parameterDTO.setId(UUID.fromString(EXISTING_PARAMETER_ID));
    parameterDTO.setName("update_parameter_partial");
    return parameterDTO;
  }

  @Test
  @Transactional
  void testFindAllParameterFacade() {
    Flux<ParameterDTO> parametersMono = parameterFacade.findAll();
    parametersMono
        .collectList()
        .blockOptional()
        .ifPresent(
            parameterDTOS -> {
              assertEquals(
                  4, parameterDTOS.size(), "Returned parameters do not match expected size");
            });
  }

  @Test
  @Transactional
  void testFindByIdParameterFacade() {
    Mono<ParameterDTO> parameterDTOMono =
        parameterFacade.findOne(UUID.fromString(EXISTING_PARAMETER_ID));

    parameterDTOMono.subscribe(
        parameterDTO -> {
          Assert.assertEquals("parameter_name", parameterDTO.getName());
        });
  }

  @Test
  @Transactional
  void testExistsByIdParameterFacade() {
    UUID existingParameterId = UUID.fromString(EXISTING_PARAMETER_ID);

    Mono<Boolean> exists = parameterFacade.existsById(existingParameterId);

    StepVerifier.create(exists).expectNext(true).verifyComplete();
  }

  @Test
  @Transactional
  void testExistsByIdNonExistingParameterFacade() {

    Mono<Boolean> exists = parameterFacade.existsById(UUID.fromString(NON_EXISTING_PARAMETER_ID));
    StepVerifier.create(exists).expectNext(false).verifyComplete();
  }

  @Test
  @Transactional
  void testSaveParameterFacade() {
    Mono<ParameterDTO> savedParameter = parameterFacade.save(getParameterDTO());
    savedParameter.subscribe(
        parameterDTO -> {
          Assert.assertEquals(getParameterDTO().getName(), parameterDTO.getName());
          parameterFacade.delete(parameterDTO.getId()).block();
        });
  }

  @Test
  @Transactional
  void testUpdateParameterFacade() {
    Mono<ParameterDTO> updateParameterDTO = parameterFacade.update(getUpdatedParameterDTO());
    updateParameterDTO.subscribe(
        parameterDistributionTypeDTO -> {
          Assert.assertEquals(
              getUpdatedParameterDTO().getName(), parameterDistributionTypeDTO.getName());
        });
  }

  @Test
  @Transactional
  void testPartialUpdateParameterFacade() {
    Mono<ParameterDTO> updateParameterDTO =
        parameterFacade.partialUpdate(getPartialUpdatedParameterDTO());
    updateParameterDTO.subscribe(
        parameterDistributionTypeDTO -> {
          Assert.assertEquals(
              getPartialUpdatedParameterDTO().getName(), parameterDistributionTypeDTO.getName());
        });
  }

  @Test
  @Transactional
  void testDeleteParameterFacade() {
    Mono<ParameterDTO> savedParameter = parameterFacade.save(getParameterDTO());

    savedParameter.subscribe(
        parameterDTO -> {
          Mono<Void> deleteResult = parameterFacade.delete(parameterDTO.getId());
          deleteResult.subscribe(
              result -> {
                Assert.assertNull(result);
                Mono<ParameterDTO> findResult = parameterFacade.findOne(parameterDTO.getId());
                findResult.subscribe(
                    notFoundParameterDTO -> Assert.assertNull(notFoundParameterDTO),
                    throwable -> Assert.assertTrue(throwable instanceof NoSuchElementException));
              });
        });
  }

  @Test
  @Transactional
  void testFindByIdNonExistingParameterFacade() {
    Mono<ParameterDTO> parameter =
        parameterFacade.findOne(UUID.fromString(NON_EXISTING_PARAMETER_ID));

    StepVerifier.create(parameter).expectError(NoSuchElementException.class).verify();
  }

  @Test
  @Transactional
  void testSaveAndUpdateParameterFacade() {
    Mono<ParameterDTO> savedParameter = parameterFacade.save(getParameterDTO());

    savedParameter.subscribe(
        parameterDTO -> {
          ParameterDTO updatedParameterDTO = getUpdatedParameterDTO();
          updatedParameterDTO.setId(parameterDTO.getId());
          Mono<ParameterDTO> updatedParameterMono = parameterFacade.save(updatedParameterDTO);

          updatedParameterMono.subscribe(
              updatedParameter -> {
                Assert.assertEquals(updatedParameter.getName(), updatedParameterDTO.getName());
                parameterFacade.delete(updatedParameter.getId()).block();
              });
        });
  }
}
