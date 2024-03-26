package ai.turintech.modelcatalog.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ai.turintech.modelcatalog.dto.ParameterTypeDTO;
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
public class ParameterTypeFacadeTest extends BasicFacadeTest {
  private final String EXISTING_PARAMETER_TYPE_ID = "1b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d27";
  private final String NON_EXISTING_PARAMETER_TYPE_ID = UUID.randomUUID().toString();

  private final String EXISTING_PARAMETER_TYPE_ID_FOR_UPDATE =
      "4b6f7a9a-4a2d-4e9a-8f2a-6d6bb9c66d21";

  @Autowired private ParameterTypeFacade parameterTypeFacade;

  private ParameterTypeDTO getParameterTypeDTO() {
    ParameterTypeDTO parameterTypeDTO = new ParameterTypeDTO();
    parameterTypeDTO.setName("test_name");
    return parameterTypeDTO;
  }

  private ParameterTypeDTO getUpdatedParameterTypeDTO() {
    ParameterTypeDTO parameterTypeDTO = new ParameterTypeDTO();
    parameterTypeDTO.setId(UUID.fromString(EXISTING_PARAMETER_TYPE_ID_FOR_UPDATE));
    parameterTypeDTO.setName("test_updated_parametertype");
    return parameterTypeDTO;
  }

  @Test
  void testFindAllParameterTypeFacade() {
    Flux<ParameterTypeDTO> parameterTypes = parameterTypeFacade.findAll();
    parameterTypes
        .collectList()
        .blockOptional()
        .ifPresent(
            parameterTypeDTOS -> {
              assertEquals(
                  4,
                  parameterTypeDTOS.size(),
                  "Returned parameter types do not match expected size");
            });
  }

  @Test
  void testFindByIdParameterTypeFacade() {
    Mono<ParameterTypeDTO> parameterTypeDTOMono =
        parameterTypeFacade.findOne(UUID.fromString(EXISTING_PARAMETER_TYPE_ID));

    parameterTypeDTOMono.subscribe(
        parameterTypeDTO -> {
          Assert.assertEquals("boolean", parameterTypeDTO.getName());
        });
  }

  @Test
  void testExistsByIdParameterTypeFacade() {
    UUID existingParameterTypeId = UUID.fromString(EXISTING_PARAMETER_TYPE_ID);

    Mono<Boolean> exists = parameterTypeFacade.existsById(existingParameterTypeId);

    StepVerifier.create(exists).expectNext(true).verifyComplete();
  }

  @Test
  void testExistsByIdNonExistingParameterTypeFacade() {

    Mono<Boolean> exists =
        parameterTypeFacade.existsById(UUID.fromString(NON_EXISTING_PARAMETER_TYPE_ID));
    StepVerifier.create(exists).expectNext(false).verifyComplete();
  }

  @Test
  void testSaveParameterTypeFacade() {
    Mono<ParameterTypeDTO> savedParameterTypeDTO = parameterTypeFacade.save(getParameterTypeDTO());
    savedParameterTypeDTO.subscribe(
        parameterTypeDTO -> {
          Assert.assertEquals(getParameterTypeDTO().getName(), parameterTypeDTO.getName());
          parameterTypeFacade.delete(parameterTypeDTO.getId()).block();
        });
  }

  @Test
  void testUpdateParameterTypeDTOFacade() {
    Mono<ParameterTypeDTO> updatedParameterTypeDTO =
        parameterTypeFacade.update(getUpdatedParameterTypeDTO());
    updatedParameterTypeDTO.subscribe(
        parameterTypeDTO -> {
          Assert.assertEquals(getUpdatedParameterTypeDTO().getName(), parameterTypeDTO.getName());
        });
  }

  @Test
  void testPartialUpdateParameterTypeDTOFacade() {
    Mono<ParameterTypeDTO> updatedParameterTypeDTO =
        parameterTypeFacade.partialUpdate(getUpdatedParameterTypeDTO());
    updatedParameterTypeDTO.subscribe(
        parameterTypeDTO -> {
          Assert.assertEquals(getUpdatedParameterTypeDTO().getName(), parameterTypeDTO.getName());
        });
  }

  @Test
  void testDeleteParameterTypeFacade() {
    Mono<ParameterTypeDTO> savedParameterType = parameterTypeFacade.save(getParameterTypeDTO());

    savedParameterType.subscribe(
        parameterTypeDTO -> {
          Mono<Void> deleteResult = parameterTypeFacade.delete(parameterTypeDTO.getId());
          deleteResult.subscribe(
              result -> {
                Assert.assertNull(result);
                Mono<ParameterTypeDTO> findResult =
                    parameterTypeFacade.findOne(parameterTypeDTO.getId());
                findResult.subscribe(
                    notFoundParameterTypeDTO -> Assert.assertNull(notFoundParameterTypeDTO),
                    throwable -> Assert.assertTrue(throwable instanceof NoSuchElementException));
              });
        });
  }

  @Test
  void testFindByIdNonExistingParameterTypeFacade() {
    Mono<ParameterTypeDTO> parameterType =
        parameterTypeFacade.findOne(UUID.fromString(NON_EXISTING_PARAMETER_TYPE_ID));

    StepVerifier.create(parameterType).expectError(NoSuchElementException.class).verify();
  }

  @Test
  void testSaveAndUpdateParameterTypeFacade() {
    Mono<ParameterTypeDTO> savedParameterType = parameterTypeFacade.save(getParameterTypeDTO());

    savedParameterType.subscribe(
        parameterTypeDTO -> {
          ParameterTypeDTO updatedParameterTypeDTO = getUpdatedParameterTypeDTO();
          updatedParameterTypeDTO.setId(parameterTypeDTO.getId());
          Mono<ParameterTypeDTO> updatedParameterTypeMono =
              parameterTypeFacade.save(updatedParameterTypeDTO);

          updatedParameterTypeMono.subscribe(
              updatedParameterType -> {
                Assert.assertEquals(
                    updatedParameterType.getName(), updatedParameterTypeDTO.getName());
                parameterTypeFacade.delete(updatedParameterType.getId()).block();
              });
        });
  }
}
