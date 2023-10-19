package ai.turintech.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ai.turintech.catalog.IntegrationTest;
import ai.turintech.catalog.domain.ModelStructureType;
import ai.turintech.catalog.repository.EntityManager;
import ai.turintech.catalog.repository.ModelStructureTypeRepository;
import ai.turintech.catalog.service.dto.ModelStructureTypeDTO;
import ai.turintech.catalog.service.mapper.ModelStructureTypeMapper;
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
 * Integration tests for the {@link ModelStructureTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ModelStructureTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/model-structure-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ModelStructureTypeRepository modelStructureTypeRepository;

    @Autowired
    private ModelStructureTypeMapper modelStructureTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ModelStructureType modelStructureType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModelStructureType createEntity(EntityManager em) {
        ModelStructureType modelStructureType = new ModelStructureType().id(UUID.randomUUID()).name(DEFAULT_NAME);
        return modelStructureType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModelStructureType createUpdatedEntity(EntityManager em) {
        ModelStructureType modelStructureType = new ModelStructureType().id(UUID.randomUUID()).name(UPDATED_NAME);
        return modelStructureType;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ModelStructureType.class).block();
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
        modelStructureType = createEntity(em);
    }

    @Test
    void createModelStructureType() throws Exception {
        int databaseSizeBeforeCreate = modelStructureTypeRepository.findAll().collectList().block().size();
        modelStructureType.setId(null);
        // Create the ModelStructureType
        ModelStructureTypeDTO modelStructureTypeDTO = modelStructureTypeMapper.toDto(modelStructureType);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelStructureTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ModelStructureType in the database
        List<ModelStructureType> modelStructureTypeList = modelStructureTypeRepository.findAll().collectList().block();
        assertThat(modelStructureTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ModelStructureType testModelStructureType = modelStructureTypeList.get(modelStructureTypeList.size() - 1);
        assertThat(testModelStructureType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createModelStructureTypeWithExistingId() throws Exception {
        // Create the ModelStructureType with an existing ID
        modelStructureTypeRepository.save(modelStructureType).block();
        ModelStructureTypeDTO modelStructureTypeDTO = modelStructureTypeMapper.toDto(modelStructureType);

        int databaseSizeBeforeCreate = modelStructureTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelStructureTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelStructureType in the database
        List<ModelStructureType> modelStructureTypeList = modelStructureTypeRepository.findAll().collectList().block();
        assertThat(modelStructureTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelStructureTypeRepository.findAll().collectList().block().size();
        // set the field null
        modelStructureType.setName(null);

        // Create the ModelStructureType, which fails.
        ModelStructureTypeDTO modelStructureTypeDTO = modelStructureTypeMapper.toDto(modelStructureType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelStructureTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ModelStructureType> modelStructureTypeList = modelStructureTypeRepository.findAll().collectList().block();
        assertThat(modelStructureTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllModelStructureTypesAsStream() {
        // Initialize the database
        modelStructureType.setId(UUID.randomUUID());
        modelStructureTypeRepository.save(modelStructureType).block();

        List<ModelStructureType> modelStructureTypeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ModelStructureTypeDTO.class)
            .getResponseBody()
            .map(modelStructureTypeMapper::toEntity)
            .filter(modelStructureType::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(modelStructureTypeList).isNotNull();
        assertThat(modelStructureTypeList).hasSize(1);
        ModelStructureType testModelStructureType = modelStructureTypeList.get(0);
        assertThat(testModelStructureType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void getAllModelStructureTypes() {
        // Initialize the database
        modelStructureType.setId(UUID.randomUUID());
        modelStructureTypeRepository.save(modelStructureType).block();

        // Get all the modelStructureTypeList
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
            .value(hasItem(modelStructureType.getId().toString()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getModelStructureType() {
        // Initialize the database
        modelStructureType.setId(UUID.randomUUID());
        modelStructureTypeRepository.save(modelStructureType).block();

        // Get the modelStructureType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, modelStructureType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(modelStructureType.getId().toString()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingModelStructureType() {
        // Get the modelStructureType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingModelStructureType() throws Exception {
        // Initialize the database
        modelStructureType.setId(UUID.randomUUID());
        modelStructureTypeRepository.save(modelStructureType).block();

        int databaseSizeBeforeUpdate = modelStructureTypeRepository.findAll().collectList().block().size();

        // Update the modelStructureType
        ModelStructureType updatedModelStructureType = modelStructureTypeRepository.findById(modelStructureType.getId()).block();
        updatedModelStructureType.name(UPDATED_NAME);
        ModelStructureTypeDTO modelStructureTypeDTO = modelStructureTypeMapper.toDto(updatedModelStructureType);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, modelStructureTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelStructureTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModelStructureType in the database
        List<ModelStructureType> modelStructureTypeList = modelStructureTypeRepository.findAll().collectList().block();
        assertThat(modelStructureTypeList).hasSize(databaseSizeBeforeUpdate);
        ModelStructureType testModelStructureType = modelStructureTypeList.get(modelStructureTypeList.size() - 1);
        assertThat(testModelStructureType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingModelStructureType() throws Exception {
        int databaseSizeBeforeUpdate = modelStructureTypeRepository.findAll().collectList().block().size();
        modelStructureType.setId(UUID.randomUUID());

        // Create the ModelStructureType
        ModelStructureTypeDTO modelStructureTypeDTO = modelStructureTypeMapper.toDto(modelStructureType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, modelStructureTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelStructureTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelStructureType in the database
        List<ModelStructureType> modelStructureTypeList = modelStructureTypeRepository.findAll().collectList().block();
        assertThat(modelStructureTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchModelStructureType() throws Exception {
        int databaseSizeBeforeUpdate = modelStructureTypeRepository.findAll().collectList().block().size();
        modelStructureType.setId(UUID.randomUUID());

        // Create the ModelStructureType
        ModelStructureTypeDTO modelStructureTypeDTO = modelStructureTypeMapper.toDto(modelStructureType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelStructureTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelStructureType in the database
        List<ModelStructureType> modelStructureTypeList = modelStructureTypeRepository.findAll().collectList().block();
        assertThat(modelStructureTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamModelStructureType() throws Exception {
        int databaseSizeBeforeUpdate = modelStructureTypeRepository.findAll().collectList().block().size();
        modelStructureType.setId(UUID.randomUUID());

        // Create the ModelStructureType
        ModelStructureTypeDTO modelStructureTypeDTO = modelStructureTypeMapper.toDto(modelStructureType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelStructureTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ModelStructureType in the database
        List<ModelStructureType> modelStructureTypeList = modelStructureTypeRepository.findAll().collectList().block();
        assertThat(modelStructureTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateModelStructureTypeWithPatch() throws Exception {
        // Initialize the database
        modelStructureType.setId(UUID.randomUUID());
        modelStructureTypeRepository.save(modelStructureType).block();

        int databaseSizeBeforeUpdate = modelStructureTypeRepository.findAll().collectList().block().size();

        // Update the modelStructureType using partial update
        ModelStructureType partialUpdatedModelStructureType = new ModelStructureType();
        partialUpdatedModelStructureType.setId(modelStructureType.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModelStructureType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedModelStructureType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModelStructureType in the database
        List<ModelStructureType> modelStructureTypeList = modelStructureTypeRepository.findAll().collectList().block();
        assertThat(modelStructureTypeList).hasSize(databaseSizeBeforeUpdate);
        ModelStructureType testModelStructureType = modelStructureTypeList.get(modelStructureTypeList.size() - 1);
        assertThat(testModelStructureType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void fullUpdateModelStructureTypeWithPatch() throws Exception {
        // Initialize the database
        modelStructureType.setId(UUID.randomUUID());
        modelStructureTypeRepository.save(modelStructureType).block();

        int databaseSizeBeforeUpdate = modelStructureTypeRepository.findAll().collectList().block().size();

        // Update the modelStructureType using partial update
        ModelStructureType partialUpdatedModelStructureType = new ModelStructureType();
        partialUpdatedModelStructureType.setId(modelStructureType.getId());

        partialUpdatedModelStructureType.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedModelStructureType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedModelStructureType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ModelStructureType in the database
        List<ModelStructureType> modelStructureTypeList = modelStructureTypeRepository.findAll().collectList().block();
        assertThat(modelStructureTypeList).hasSize(databaseSizeBeforeUpdate);
        ModelStructureType testModelStructureType = modelStructureTypeList.get(modelStructureTypeList.size() - 1);
        assertThat(testModelStructureType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingModelStructureType() throws Exception {
        int databaseSizeBeforeUpdate = modelStructureTypeRepository.findAll().collectList().block().size();
        modelStructureType.setId(UUID.randomUUID());

        // Create the ModelStructureType
        ModelStructureTypeDTO modelStructureTypeDTO = modelStructureTypeMapper.toDto(modelStructureType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, modelStructureTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelStructureTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelStructureType in the database
        List<ModelStructureType> modelStructureTypeList = modelStructureTypeRepository.findAll().collectList().block();
        assertThat(modelStructureTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchModelStructureType() throws Exception {
        int databaseSizeBeforeUpdate = modelStructureTypeRepository.findAll().collectList().block().size();
        modelStructureType.setId(UUID.randomUUID());

        // Create the ModelStructureType
        ModelStructureTypeDTO modelStructureTypeDTO = modelStructureTypeMapper.toDto(modelStructureType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelStructureTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ModelStructureType in the database
        List<ModelStructureType> modelStructureTypeList = modelStructureTypeRepository.findAll().collectList().block();
        assertThat(modelStructureTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamModelStructureType() throws Exception {
        int databaseSizeBeforeUpdate = modelStructureTypeRepository.findAll().collectList().block().size();
        modelStructureType.setId(UUID.randomUUID());

        // Create the ModelStructureType
        ModelStructureTypeDTO modelStructureTypeDTO = modelStructureTypeMapper.toDto(modelStructureType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(modelStructureTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ModelStructureType in the database
        List<ModelStructureType> modelStructureTypeList = modelStructureTypeRepository.findAll().collectList().block();
        assertThat(modelStructureTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteModelStructureType() {
        // Initialize the database
        modelStructureType.setId(UUID.randomUUID());
        modelStructureTypeRepository.save(modelStructureType).block();

        int databaseSizeBeforeDelete = modelStructureTypeRepository.findAll().collectList().block().size();

        // Delete the modelStructureType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, modelStructureType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ModelStructureType> modelStructureTypeList = modelStructureTypeRepository.findAll().collectList().block();
        assertThat(modelStructureTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
