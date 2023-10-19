package ai.turintech.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ai.turintech.catalog.IntegrationTest;
import ai.turintech.catalog.domain.ParameterTypeDefinition;
import ai.turintech.catalog.repository.EntityManager;
import ai.turintech.catalog.repository.ParameterTypeDefinitionRepository;
import ai.turintech.catalog.service.dto.ParameterTypeDefinitionDTO;
import ai.turintech.catalog.service.mapper.ParameterTypeDefinitionMapper;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ParameterTypeDefinitionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ParameterTypeDefinitionResourceIT {

    private static final Integer DEFAULT_ORDERING = 1;
    private static final Integer UPDATED_ORDERING = 2;

    private static final String ENTITY_API_URL = "/api/parameter-type-definitions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ParameterTypeDefinitionRepository parameterTypeDefinitionRepository;

    @Autowired
    private ParameterTypeDefinitionMapper parameterTypeDefinitionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ParameterTypeDefinition parameterTypeDefinition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParameterTypeDefinition createEntity(EntityManager em) {
        ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition().id(UUID.randomUUID()).ordering(DEFAULT_ORDERING);
        return parameterTypeDefinition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParameterTypeDefinition createUpdatedEntity(EntityManager em) {
        ParameterTypeDefinition parameterTypeDefinition = new ParameterTypeDefinition().id(UUID.randomUUID()).ordering(UPDATED_ORDERING);
        return parameterTypeDefinition;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ParameterTypeDefinition.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        parameterTypeDefinition = createEntity(em);
    }

    @Test
    void createParameterTypeDefinition() throws Exception {
        int databaseSizeBeforeCreate = parameterTypeDefinitionRepository.findAll().collectList().block().size();
        parameterTypeDefinition.setId(null);
        // Create the ParameterTypeDefinition
        ParameterTypeDefinitionDTO parameterTypeDefinitionDTO = parameterTypeDefinitionMapper.toDto(parameterTypeDefinition);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDefinitionDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ParameterTypeDefinition in the database
        List<ParameterTypeDefinition> parameterTypeDefinitionList = parameterTypeDefinitionRepository.findAll().collectList().block();
        assertThat(parameterTypeDefinitionList).hasSize(databaseSizeBeforeCreate + 1);
        ParameterTypeDefinition testParameterTypeDefinition = parameterTypeDefinitionList.get(parameterTypeDefinitionList.size() - 1);
        assertThat(testParameterTypeDefinition.getOrdering()).isEqualTo(DEFAULT_ORDERING);
    }

    @Test
    void createParameterTypeDefinitionWithExistingId() throws Exception {
        // Create the ParameterTypeDefinition with an existing ID
        parameterTypeDefinitionRepository.save(parameterTypeDefinition).block();
        ParameterTypeDefinitionDTO parameterTypeDefinitionDTO = parameterTypeDefinitionMapper.toDto(parameterTypeDefinition);

        int databaseSizeBeforeCreate = parameterTypeDefinitionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDefinitionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParameterTypeDefinition in the database
        List<ParameterTypeDefinition> parameterTypeDefinitionList = parameterTypeDefinitionRepository.findAll().collectList().block();
        assertThat(parameterTypeDefinitionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkOrderingIsRequired() throws Exception {
        int databaseSizeBeforeTest = parameterTypeDefinitionRepository.findAll().collectList().block().size();
        // set the field null
        parameterTypeDefinition.setOrdering(null);

        // Create the ParameterTypeDefinition, which fails.
        ParameterTypeDefinitionDTO parameterTypeDefinitionDTO = parameterTypeDefinitionMapper.toDto(parameterTypeDefinition);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDefinitionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ParameterTypeDefinition> parameterTypeDefinitionList = parameterTypeDefinitionRepository.findAll().collectList().block();
        assertThat(parameterTypeDefinitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllParameterTypeDefinitionsAsStream() {
        // Initialize the database
        parameterTypeDefinition.setId(UUID.randomUUID());
        parameterTypeDefinitionRepository.save(parameterTypeDefinition).block();

        List<ParameterTypeDefinition> parameterTypeDefinitionList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ParameterTypeDefinitionDTO.class)
            .getResponseBody()
            .map(parameterTypeDefinitionMapper::toEntity)
            .filter(parameterTypeDefinition::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(parameterTypeDefinitionList).isNotNull();
        assertThat(parameterTypeDefinitionList).hasSize(1);
        ParameterTypeDefinition testParameterTypeDefinition = parameterTypeDefinitionList.get(0);
        assertThat(testParameterTypeDefinition.getOrdering()).isEqualTo(DEFAULT_ORDERING);
    }

    @Test
    void getAllParameterTypeDefinitions() {
        // Initialize the database
        parameterTypeDefinition.setId(UUID.randomUUID());
        parameterTypeDefinitionRepository.save(parameterTypeDefinition).block();

        // Get all the parameterTypeDefinitionList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(parameterTypeDefinition.getId().toString()))
            .jsonPath("$.[*].ordering")
            .value(hasItem(DEFAULT_ORDERING));
    }

    @Test
    void getParameterTypeDefinition() {
        // Initialize the database
        parameterTypeDefinition.setId(UUID.randomUUID());
        parameterTypeDefinitionRepository.save(parameterTypeDefinition).block();

        // Get the parameterTypeDefinition
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, parameterTypeDefinition.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(parameterTypeDefinition.getId().toString()))
            .jsonPath("$.ordering")
            .value(is(DEFAULT_ORDERING));
    }

    @Test
    void getNonExistingParameterTypeDefinition() {
        // Get the parameterTypeDefinition
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingParameterTypeDefinition() throws Exception {
        // Initialize the database
        parameterTypeDefinition.setId(UUID.randomUUID());
        parameterTypeDefinitionRepository.save(parameterTypeDefinition).block();

        int databaseSizeBeforeUpdate = parameterTypeDefinitionRepository.findAll().collectList().block().size();

        // Update the parameterTypeDefinition
        ParameterTypeDefinition updatedParameterTypeDefinition = parameterTypeDefinitionRepository
            .findById(parameterTypeDefinition.getId())
            .block();
        updatedParameterTypeDefinition.ordering(UPDATED_ORDERING);
        ParameterTypeDefinitionDTO parameterTypeDefinitionDTO = parameterTypeDefinitionMapper.toDto(updatedParameterTypeDefinition);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, parameterTypeDefinitionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDefinitionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ParameterTypeDefinition in the database
        List<ParameterTypeDefinition> parameterTypeDefinitionList = parameterTypeDefinitionRepository.findAll().collectList().block();
        assertThat(parameterTypeDefinitionList).hasSize(databaseSizeBeforeUpdate);
        ParameterTypeDefinition testParameterTypeDefinition = parameterTypeDefinitionList.get(parameterTypeDefinitionList.size() - 1);
        assertThat(testParameterTypeDefinition.getOrdering()).isEqualTo(UPDATED_ORDERING);
    }

    @Test
    void putNonExistingParameterTypeDefinition() throws Exception {
        int databaseSizeBeforeUpdate = parameterTypeDefinitionRepository.findAll().collectList().block().size();
        parameterTypeDefinition.setId(UUID.randomUUID());

        // Create the ParameterTypeDefinition
        ParameterTypeDefinitionDTO parameterTypeDefinitionDTO = parameterTypeDefinitionMapper.toDto(parameterTypeDefinition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, parameterTypeDefinitionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDefinitionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParameterTypeDefinition in the database
        List<ParameterTypeDefinition> parameterTypeDefinitionList = parameterTypeDefinitionRepository.findAll().collectList().block();
        assertThat(parameterTypeDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchParameterTypeDefinition() throws Exception {
        int databaseSizeBeforeUpdate = parameterTypeDefinitionRepository.findAll().collectList().block().size();
        parameterTypeDefinition.setId(UUID.randomUUID());

        // Create the ParameterTypeDefinition
        ParameterTypeDefinitionDTO parameterTypeDefinitionDTO = parameterTypeDefinitionMapper.toDto(parameterTypeDefinition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDefinitionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParameterTypeDefinition in the database
        List<ParameterTypeDefinition> parameterTypeDefinitionList = parameterTypeDefinitionRepository.findAll().collectList().block();
        assertThat(parameterTypeDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamParameterTypeDefinition() throws Exception {
        int databaseSizeBeforeUpdate = parameterTypeDefinitionRepository.findAll().collectList().block().size();
        parameterTypeDefinition.setId(UUID.randomUUID());

        // Create the ParameterTypeDefinition
        ParameterTypeDefinitionDTO parameterTypeDefinitionDTO = parameterTypeDefinitionMapper.toDto(parameterTypeDefinition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDefinitionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ParameterTypeDefinition in the database
        List<ParameterTypeDefinition> parameterTypeDefinitionList = parameterTypeDefinitionRepository.findAll().collectList().block();
        assertThat(parameterTypeDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateParameterTypeDefinitionWithPatch() throws Exception {
        // Initialize the database
        parameterTypeDefinition.setId(UUID.randomUUID());
        parameterTypeDefinitionRepository.save(parameterTypeDefinition).block();

        int databaseSizeBeforeUpdate = parameterTypeDefinitionRepository.findAll().collectList().block().size();

        // Update the parameterTypeDefinition using partial update
        ParameterTypeDefinition partialUpdatedParameterTypeDefinition = new ParameterTypeDefinition();
        partialUpdatedParameterTypeDefinition.setId(parameterTypeDefinition.getId());

        partialUpdatedParameterTypeDefinition.ordering(UPDATED_ORDERING);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedParameterTypeDefinition.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedParameterTypeDefinition))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ParameterTypeDefinition in the database
        List<ParameterTypeDefinition> parameterTypeDefinitionList = parameterTypeDefinitionRepository.findAll().collectList().block();
        assertThat(parameterTypeDefinitionList).hasSize(databaseSizeBeforeUpdate);
        ParameterTypeDefinition testParameterTypeDefinition = parameterTypeDefinitionList.get(parameterTypeDefinitionList.size() - 1);
        assertThat(testParameterTypeDefinition.getOrdering()).isEqualTo(UPDATED_ORDERING);
    }

    @Test
    void fullUpdateParameterTypeDefinitionWithPatch() throws Exception {
        // Initialize the database
        parameterTypeDefinition.setId(UUID.randomUUID());
        parameterTypeDefinitionRepository.save(parameterTypeDefinition).block();

        int databaseSizeBeforeUpdate = parameterTypeDefinitionRepository.findAll().collectList().block().size();

        // Update the parameterTypeDefinition using partial update
        ParameterTypeDefinition partialUpdatedParameterTypeDefinition = new ParameterTypeDefinition();
        partialUpdatedParameterTypeDefinition.setId(parameterTypeDefinition.getId());

        partialUpdatedParameterTypeDefinition.ordering(UPDATED_ORDERING);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedParameterTypeDefinition.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedParameterTypeDefinition))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ParameterTypeDefinition in the database
        List<ParameterTypeDefinition> parameterTypeDefinitionList = parameterTypeDefinitionRepository.findAll().collectList().block();
        assertThat(parameterTypeDefinitionList).hasSize(databaseSizeBeforeUpdate);
        ParameterTypeDefinition testParameterTypeDefinition = parameterTypeDefinitionList.get(parameterTypeDefinitionList.size() - 1);
        assertThat(testParameterTypeDefinition.getOrdering()).isEqualTo(UPDATED_ORDERING);
    }

    @Test
    void patchNonExistingParameterTypeDefinition() throws Exception {
        int databaseSizeBeforeUpdate = parameterTypeDefinitionRepository.findAll().collectList().block().size();
        parameterTypeDefinition.setId(UUID.randomUUID());

        // Create the ParameterTypeDefinition
        ParameterTypeDefinitionDTO parameterTypeDefinitionDTO = parameterTypeDefinitionMapper.toDto(parameterTypeDefinition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, parameterTypeDefinitionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDefinitionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParameterTypeDefinition in the database
        List<ParameterTypeDefinition> parameterTypeDefinitionList = parameterTypeDefinitionRepository.findAll().collectList().block();
        assertThat(parameterTypeDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchParameterTypeDefinition() throws Exception {
        int databaseSizeBeforeUpdate = parameterTypeDefinitionRepository.findAll().collectList().block().size();
        parameterTypeDefinition.setId(UUID.randomUUID());

        // Create the ParameterTypeDefinition
        ParameterTypeDefinitionDTO parameterTypeDefinitionDTO = parameterTypeDefinitionMapper.toDto(parameterTypeDefinition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDefinitionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParameterTypeDefinition in the database
        List<ParameterTypeDefinition> parameterTypeDefinitionList = parameterTypeDefinitionRepository.findAll().collectList().block();
        assertThat(parameterTypeDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamParameterTypeDefinition() throws Exception {
        int databaseSizeBeforeUpdate = parameterTypeDefinitionRepository.findAll().collectList().block().size();
        parameterTypeDefinition.setId(UUID.randomUUID());

        // Create the ParameterTypeDefinition
        ParameterTypeDefinitionDTO parameterTypeDefinitionDTO = parameterTypeDefinitionMapper.toDto(parameterTypeDefinition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDefinitionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ParameterTypeDefinition in the database
        List<ParameterTypeDefinition> parameterTypeDefinitionList = parameterTypeDefinitionRepository.findAll().collectList().block();
        assertThat(parameterTypeDefinitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteParameterTypeDefinition() {
        // Initialize the database
        parameterTypeDefinition.setId(UUID.randomUUID());
        parameterTypeDefinitionRepository.save(parameterTypeDefinition).block();

        int databaseSizeBeforeDelete = parameterTypeDefinitionRepository.findAll().collectList().block().size();

        // Delete the parameterTypeDefinition
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, parameterTypeDefinition.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ParameterTypeDefinition> parameterTypeDefinitionList = parameterTypeDefinitionRepository.findAll().collectList().block();
        assertThat(parameterTypeDefinitionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
