package ai.turintech.modelcatalog.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ai.turintech.modelcatalog.dto.CategoricalParameterDTO;
import ai.turintech.modelcatalog.dto.ParameterTypeDefinitionDTO;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class CategoricalParameterFacadeTest extends BasicFacadeTest {
  @Autowired private CategoricalParameterFacade categoricalParameterFacade;

  private static final String PARAMETER_TYPE_ID = "323e4567-e89b-12d3-a456-426614174003";
  private static final String EXISTING_PARAMETER_ID = "323e4567-e89b-12d3-a456-426614174003";

  private CategoricalParameterDTO getCategoricalParameterDTO() {
    ParameterTypeDefinitionDTO parameterTypeDefinitionDTO = new ParameterTypeDefinitionDTO();
    parameterTypeDefinitionDTO.setId(UUID.fromString(PARAMETER_TYPE_ID));
    parameterTypeDefinitionDTO.setOrdering(10);

    CategoricalParameterDTO categoricalParameterDTO = new CategoricalParameterDTO();
    categoricalParameterDTO.setId(UUID.fromString(EXISTING_PARAMETER_ID));
    categoricalParameterDTO.setDefaultValue("test_default_value");
    return categoricalParameterDTO;
  }

  @Test
  @Transactional
  void testFindAllCategoricalParameterFacade() {
    Flux<CategoricalParameterDTO> categoricalParametersMono = categoricalParameterFacade.findAll();
    StepVerifier.create(categoricalParametersMono.collectList())
        .assertNext(
            categoricalParameterDTOS ->
                assertEquals(
                    2,
                    categoricalParameterDTOS.size(),
                    "Returned categorical parameters do not match expected size"))
        .verifyComplete();
  }

  @Test
  @Transactional
  void testFindByIdCategoricalParameterFacade() {
    Mono<CategoricalParameterDTO> categoricalParameterDTOMono =
        categoricalParameterFacade.findOne(UUID.fromString(EXISTING_PARAMETER_ID));
    StepVerifier.create(categoricalParameterDTOMono)
        .assertNext(
            categoricalParameterDTO ->
                assertEquals("value1", categoricalParameterDTO.getDefaultValue()))
        .verifyComplete();
  }

  @Test
  void testExistsByIdCategoricalParameterFacade() {
    Mono<Boolean> exists =
        categoricalParameterFacade.existsById(UUID.fromString(EXISTING_PARAMETER_ID));
    StepVerifier.create(exists).expectNext(true).verifyComplete();
  }

  @Test
  void testExistsByIdNonExistingCategoricalParameterFacade() {
    Mono<Boolean> exists = categoricalParameterFacade.existsById(UUID.randomUUID());
    StepVerifier.create(exists).expectNext(false).verifyComplete();
  }

  @Test
  @Transactional
  void testDeleteCategoricalParameterFacade() {
    Mono<CategoricalParameterDTO> savedCategoricalParameter =
        categoricalParameterFacade.save(getCategoricalParameterDTO());

    StepVerifier.create(savedCategoricalParameter)
        .assertNext(
            categoricalParameterDTO -> {
              StepVerifier.create(
                      categoricalParameterFacade.delete(categoricalParameterDTO.getId()))
                  .expectNextCount(0)
                  .verifyComplete();

              StepVerifier.create(
                      categoricalParameterFacade.findOne(categoricalParameterDTO.getId()))
                  .expectError(NoSuchElementException.class)
                  .verify();
            })
        .verifyComplete();
  }

  @Test
  @Transactional
  void testFindByIdNonExistingCategoricalParameterFacade() {
    Mono<CategoricalParameterDTO> categoricalParameter =
        categoricalParameterFacade.findOne(UUID.randomUUID());

    StepVerifier.create(categoricalParameter).expectError(NoSuchElementException.class).verify();
  }
}
