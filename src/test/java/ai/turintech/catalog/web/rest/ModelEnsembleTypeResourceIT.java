package ai.turintech.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ai.turintech.catalog.IntegrationTest;
import ai.turintech.catalog.domain.ModelEnsembleType;
import ai.turintech.catalog.repository.EntityManager;
import ai.turintech.catalog.repository.ModelEnsembleTypeRepository;
import ai.turintech.catalog.service.dto.ModelEnsembleTypeDTO;
import ai.turintech.catalog.service.mapper.ModelEnsembleTypeMapper;
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
 * Integration tests for the {@link ModelEnsembleTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ModelEnsembleTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/model-ensemble-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ModelEnsembleTypeRepository modelEnsembleTypeRepository;

    @Autowired
    private ModelEnsembleTypeMapper modelEnsembleTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ModelEnsembleType modelEnsembleType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModelEnsembleType createEntity(EntityManager em) {
        ModelEnsembleType modelEnsembleType = new ModelEnsembleType().id(UUID.randomUUID()).name(DEFAULT_NAME);
        return modelEnsembleType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModelEnsembleType createUpdatedEntity(EntityManager em) {
        ModelEnsembleType modelEnsembleType = new ModelEnsembleType().id(UUID.randomUUID()).name(UPDATED_NAME);
        return modelEnsembleType;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ModelEnsembleType.class).block();
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
        modelEnsembleType = createEntity(em);
    }

    @Test
    void createModelEnsembleType() throws Exception {
        int databaseSizeBeforeCreate = modelEnsembleTypeRepository.findAll().collectList().block().size();
        modelEnsembleType.setId(null);
        // Create the ModelEnsembleType
        ModelEnsembleTypeDTO modelEnsembleTypeDTO = modelEnsembleTypeMapper.toDto(modelEnsembleType);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelEnsembleTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ModelEnsembleType in the database
        List<ModelEnsembleType> modelEnsembleTypeList = modelEnsembleTypeRepository.findAll().collectList().block();
        assertThat(modelEnsembleTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ModelEnsembleType testModelEnsembleType = modelEnsembleTypeList.get(modelEnsembleTypeList.size() - 1);
        assertThat(testModelEnsembleType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createModelEnsembleTypeWithExistingId() throws Exception {
        // Create the ModelEnsembleType with an existing ID
        modelEnsembleTypeRepository.save(modelEnsembleType).block();
        ModelEnsembleTypeDTO modelEnsembleTypeDTO = modelEnsembleTypeMapper.toDto(modelEnsembleType);

        int databaseSizeBeforeCreate = modelEnsembleTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelEnsembleTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelEnsembleType in the database
        List<ModelEnsembleType> modelEnsembleTypeList = modelEnsembleTypeRepository.findAll().collectList().block();
        assertThat(modelEnsembleTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelEnsembleTypeRepository.findAll().collectList().block().size();
        // set the field null
        modelEnsembleType.setName(null);

        // Create the ModelEnsembleType, which fails.
        ModelEnsembleTypeDTO modelEnsembleTypeDTO = modelEnsembleTypeMapper.toDto(modelEnsembleType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelEnsembleTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ModelEnsembleType> modelEnsembleTypeList = modelEnsembleTypeRepository.findAll().collectList().block();
        assertThat(modelEnsembleTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllModelEnsembleTypesAsStream() {
        // Initialize the database
        modelEnsembleType.setId(UUID.randomUUID());
        modelEnsembleTypeRepository.save(modelEnsembleType).block();

        List<ModelEnsembleType> modelEnsembleTypeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ModelEnsembleTypeDTO.class)
            .getResponseBody()
            .map(modelEnsembleTypeMapper::toEntity)
            .filter(modelEnsembleType::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(modelEnsembleTypeList).isNotNull();
        assertThat(modelEnsembleTypeList).hasSize(1);
        ModelEnsembleType testModelEnsembleType = modelEnsembleTypeList.get(0);
        assertThat(testModelEnsembleType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void getAllModelEnsembleTypes() {
        // Initialize the database
        modelEnsembleType.setId(UUID.randomUUID());
        modelEnsembleTypeRepository.save(modelEnsembleType).block();

        // Get all the modelEnsembleTypeList
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
            .value(hasItem(modelEnsembleType.getId().toString()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getModelEnsembleType() {
        // Initialize the database
        modelEnsembleType.setId(UUID.randomUUID());
        modelEnsembleTypeRepository.save(modelEnsembleType).block();

        // Get the modelEnsembleType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, modelEnsembleType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(modelEnsembleType.getId().toString()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingModelEnsembleType() {
        // Get the modelEnsembleType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingModelEnsembleType() throws Exception {
        // Initialize the database
        modelEnsembleType.setId(UUID.randomUUID());
        modelEnsembleTypeRepository.save(modelEnsembleType).block();

        int databaseSizeBeforeUpdate = modelEnsembleTypeRepository.findAll().collectList().block().size();

        // Update the modelEnsembleType
        ModelEnsembleType updatedModelEnsembleType = modelEnsembleTypeRepository.findById(modelEnsembleType.getId()).block();
        updatedModelEnsembleType.name(UPDATED_NAME);
        ModelEnsembleTypeDTO modelEnsembleTypeDTO = modelEnsembleTypeMapper.toDto(updatedModelEnsembleType);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, modelEnsembleTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelEnsembleTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModelEnsembleType in the database
        List<ModelEnsembleType> modelEnsembleTypeList = modelEnsembleTypeRepository.findAll().collectList().block();
        assertThat(modelEnsembleTypeList).hasSize(databaseSizeBeforeUpdate);
        ModelEnsembleType testModelEnsembleType = modelEnsembleTypeList.get(modelEnsembleTypeList.size() - 1);
        assertThat(testModelEnsembleType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingModelEnsembleType() throws Exception {
        int databaseSizeBeforeUpdate = modelEnsembleTypeRepository.findAll().collectList().block().size();
        modelEnsembleType.setId(UUID.randomUUID());

        // Create the ModelEnsembleType
        ModelEnsembleTypeDTO modelEnsembleTypeDTO = modelEnsembleTypeMapper.toDto(modelEnsembleType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, modelEnsembleTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelEnsembleTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelEnsembleType in the database
        List<ModelEnsembleType> modelEnsembleTypeList = modelEnsembleTypeRepository.findAll().collectList().block();
        assertThat(modelEnsembleTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchModelEnsembleType() throws Exception {
        int databaseSizeBeforeUpdate = modelEnsembleTypeRepository.findAll().collectList().block().size();
        modelEnsembleType.setId(UUID.randomUUID());

        // Create the ModelEnsembleType
        ModelEnsembleTypeDTO modelEnsembleTypeDTO = modelEnsembleTypeMapper.toDto(modelEnsembleType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelEnsembleTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelEnsembleType in the database
        List<ModelEnsembleType> modelEnsembleTypeList = modelEnsembleTypeRepository.findAll().collectList().block();
        assertThat(modelEnsembleTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamModelEnsembleType() throws Exception {
        int databaseSizeBeforeUpdate = modelEnsembleTypeRepository.findAll().collectList().block().size();
        modelEnsembleType.setId(UUID.randomUUID());

        // Create the ModelEnsembleType
        ModelEnsembleTypeDTO modelEnsembleTypeDTO = modelEnsembleTypeMapper.toDto(modelEnsembleType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelEnsembleTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ModelEnsembleType in the database
        List<ModelEnsembleType> modelEnsembleTypeList = modelEnsembleTypeRepository.findAll().collectList().block();
        assertThat(modelEnsembleTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateModelEnsembleTypeWithPatch() throws Exception {
        // Initialize the database
        modelEnsembleType.setId(UUID.randomUUID());
        modelEnsembleTypeRepository.save(modelEnsembleType).block();

        int databaseSizeBeforeUpdate = modelEnsembleTypeRepository.findAll().collectList().block().size();

        // Update the modelEnsembleType using partial update
        ModelEnsembleType partialUpdatedModelEnsembleType = new ModelEnsembleType();
        partialUpdatedModelEnsembleType.setId(modelEnsembleType.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModelEnsembleType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedModelEnsembleType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModelEnsembleType in the database
        List<ModelEnsembleType> modelEnsembleTypeList = modelEnsembleTypeRepository.findAll().collectList().block();
        assertThat(modelEnsembleTypeList).hasSize(databaseSizeBeforeUpdate);
        ModelEnsembleType testModelEnsembleType = modelEnsembleTypeList.get(modelEnsembleTypeList.size() - 1);
        assertThat(testModelEnsembleType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void fullUpdateModelEnsembleTypeWithPatch() throws Exception {
        // Initialize the database
        modelEnsembleType.setId(UUID.randomUUID());
        modelEnsembleTypeRepository.save(modelEnsembleType).block();

        int databaseSizeBeforeUpdate = modelEnsembleTypeRepository.findAll().collectList().block().size();

        // Update the modelEnsembleType using partial update
        ModelEnsembleType partialUpdatedModelEnsembleType = new ModelEnsembleType();
        partialUpdatedModelEnsembleType.setId(modelEnsembleType.getId());

        partialUpdatedModelEnsembleType.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModelEnsembleType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedModelEnsembleType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModelEnsembleType in the database
        List<ModelEnsembleType> modelEnsembleTypeList = modelEnsembleTypeRepository.findAll().collectList().block();
        assertThat(modelEnsembleTypeList).hasSize(databaseSizeBeforeUpdate);
        ModelEnsembleType testModelEnsembleType = modelEnsembleTypeList.get(modelEnsembleTypeList.size() - 1);
        assertThat(testModelEnsembleType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingModelEnsembleType() throws Exception {
        int databaseSizeBeforeUpdate = modelEnsembleTypeRepository.findAll().collectList().block().size();
        modelEnsembleType.setId(UUID.randomUUID());

        // Create the ModelEnsembleType
        ModelEnsembleTypeDTO modelEnsembleTypeDTO = modelEnsembleTypeMapper.toDto(modelEnsembleType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, modelEnsembleTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelEnsembleTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelEnsembleType in the database
        List<ModelEnsembleType> modelEnsembleTypeList = modelEnsembleTypeRepository.findAll().collectList().block();
        assertThat(modelEnsembleTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchModelEnsembleType() throws Exception {
        int databaseSizeBeforeUpdate = modelEnsembleTypeRepository.findAll().collectList().block().size();
        modelEnsembleType.setId(UUID.randomUUID());

        // Create the ModelEnsembleType
        ModelEnsembleTypeDTO modelEnsembleTypeDTO = modelEnsembleTypeMapper.toDto(modelEnsembleType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelEnsembleTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelEnsembleType in the database
        List<ModelEnsembleType> modelEnsembleTypeList = modelEnsembleTypeRepository.findAll().collectList().block();
        assertThat(modelEnsembleTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamModelEnsembleType() throws Exception {
        int databaseSizeBeforeUpdate = modelEnsembleTypeRepository.findAll().collectList().block().size();
        modelEnsembleType.setId(UUID.randomUUID());

        // Create the ModelEnsembleType
        ModelEnsembleTypeDTO modelEnsembleTypeDTO = modelEnsembleTypeMapper.toDto(modelEnsembleType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelEnsembleTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ModelEnsembleType in the database
        List<ModelEnsembleType> modelEnsembleTypeList = modelEnsembleTypeRepository.findAll().collectList().block();
        assertThat(modelEnsembleTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteModelEnsembleType() {
        // Initialize the database
        modelEnsembleType.setId(UUID.randomUUID());
        modelEnsembleTypeRepository.save(modelEnsembleType).block();

        int databaseSizeBeforeDelete = modelEnsembleTypeRepository.findAll().collectList().block().size();

        // Delete the modelEnsembleType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, modelEnsembleType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ModelEnsembleType> modelEnsembleTypeList = modelEnsembleTypeRepository.findAll().collectList().block();
        assertThat(modelEnsembleTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
