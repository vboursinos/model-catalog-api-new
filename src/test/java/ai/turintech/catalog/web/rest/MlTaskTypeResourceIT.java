package ai.turintech.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ai.turintech.catalog.IntegrationTest;
import ai.turintech.catalog.domain.MlTaskType;
import ai.turintech.catalog.repository.EntityManager;
import ai.turintech.catalog.repository.MlTaskTypeRepository;
import ai.turintech.catalog.service.dto.MlTaskTypeDTO;
import ai.turintech.catalog.service.mapper.MlTaskTypeMapper;
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
 * Integration tests for the {@link MlTaskTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MlTaskTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ml-task-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private MlTaskTypeRepository mlTaskTypeRepository;

    @Autowired
    private MlTaskTypeMapper mlTaskTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private MlTaskType mlTaskType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MlTaskType createEntity(EntityManager em) {
        MlTaskType mlTaskType = new MlTaskType().id(UUID.randomUUID()).name(DEFAULT_NAME);
        return mlTaskType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MlTaskType createUpdatedEntity(EntityManager em) {
        MlTaskType mlTaskType = new MlTaskType().id(UUID.randomUUID()).name(UPDATED_NAME);
        return mlTaskType;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(MlTaskType.class).block();
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
        mlTaskType = createEntity(em);
    }

    @Test
    void createMlTaskType() throws Exception {
        int databaseSizeBeforeCreate = mlTaskTypeRepository.findAll().collectList().block().size();
        mlTaskType.setId(null);
        // Create the MlTaskType
        MlTaskTypeDTO mlTaskTypeDTO = mlTaskTypeMapper.toDto(mlTaskType);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mlTaskTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the MlTaskType in the database
        List<MlTaskType> mlTaskTypeList = mlTaskTypeRepository.findAll().collectList().block();
        assertThat(mlTaskTypeList).hasSize(databaseSizeBeforeCreate + 1);
        MlTaskType testMlTaskType = mlTaskTypeList.get(mlTaskTypeList.size() - 1);
        assertThat(testMlTaskType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createMlTaskTypeWithExistingId() throws Exception {
        // Create the MlTaskType with an existing ID
        mlTaskTypeRepository.save(mlTaskType).block();
        MlTaskTypeDTO mlTaskTypeDTO = mlTaskTypeMapper.toDto(mlTaskType);

        int databaseSizeBeforeCreate = mlTaskTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mlTaskTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MlTaskType in the database
        List<MlTaskType> mlTaskTypeList = mlTaskTypeRepository.findAll().collectList().block();
        assertThat(mlTaskTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = mlTaskTypeRepository.findAll().collectList().block().size();
        // set the field null
        mlTaskType.setName(null);

        // Create the MlTaskType, which fails.
        MlTaskTypeDTO mlTaskTypeDTO = mlTaskTypeMapper.toDto(mlTaskType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mlTaskTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<MlTaskType> mlTaskTypeList = mlTaskTypeRepository.findAll().collectList().block();
        assertThat(mlTaskTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllMlTaskTypesAsStream() {
        // Initialize the database
        mlTaskType.setId(UUID.randomUUID());
        mlTaskTypeRepository.save(mlTaskType).block();

        List<MlTaskType> mlTaskTypeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(MlTaskTypeDTO.class)
            .getResponseBody()
            .map(mlTaskTypeMapper::toEntity)
            .filter(mlTaskType::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(mlTaskTypeList).isNotNull();
        assertThat(mlTaskTypeList).hasSize(1);
        MlTaskType testMlTaskType = mlTaskTypeList.get(0);
        assertThat(testMlTaskType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void getAllMlTaskTypes() {
        // Initialize the database
        mlTaskType.setId(UUID.randomUUID());
        mlTaskTypeRepository.save(mlTaskType).block();

        // Get all the mlTaskTypeList
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
            .value(hasItem(mlTaskType.getId().toString()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getMlTaskType() {
        // Initialize the database
        mlTaskType.setId(UUID.randomUUID());
        mlTaskTypeRepository.save(mlTaskType).block();

        // Get the mlTaskType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, mlTaskType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(mlTaskType.getId().toString()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingMlTaskType() {
        // Get the mlTaskType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMlTaskType() throws Exception {
        // Initialize the database
        mlTaskType.setId(UUID.randomUUID());
        mlTaskTypeRepository.save(mlTaskType).block();

        int databaseSizeBeforeUpdate = mlTaskTypeRepository.findAll().collectList().block().size();

        // Update the mlTaskType
        MlTaskType updatedMlTaskType = mlTaskTypeRepository.findById(mlTaskType.getId()).block();
        updatedMlTaskType.name(UPDATED_NAME);
        MlTaskTypeDTO mlTaskTypeDTO = mlTaskTypeMapper.toDto(updatedMlTaskType);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, mlTaskTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mlTaskTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MlTaskType in the database
        List<MlTaskType> mlTaskTypeList = mlTaskTypeRepository.findAll().collectList().block();
        assertThat(mlTaskTypeList).hasSize(databaseSizeBeforeUpdate);
        MlTaskType testMlTaskType = mlTaskTypeList.get(mlTaskTypeList.size() - 1);
        assertThat(testMlTaskType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingMlTaskType() throws Exception {
        int databaseSizeBeforeUpdate = mlTaskTypeRepository.findAll().collectList().block().size();
        mlTaskType.setId(UUID.randomUUID());

        // Create the MlTaskType
        MlTaskTypeDTO mlTaskTypeDTO = mlTaskTypeMapper.toDto(mlTaskType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, mlTaskTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mlTaskTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MlTaskType in the database
        List<MlTaskType> mlTaskTypeList = mlTaskTypeRepository.findAll().collectList().block();
        assertThat(mlTaskTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMlTaskType() throws Exception {
        int databaseSizeBeforeUpdate = mlTaskTypeRepository.findAll().collectList().block().size();
        mlTaskType.setId(UUID.randomUUID());

        // Create the MlTaskType
        MlTaskTypeDTO mlTaskTypeDTO = mlTaskTypeMapper.toDto(mlTaskType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mlTaskTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MlTaskType in the database
        List<MlTaskType> mlTaskTypeList = mlTaskTypeRepository.findAll().collectList().block();
        assertThat(mlTaskTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMlTaskType() throws Exception {
        int databaseSizeBeforeUpdate = mlTaskTypeRepository.findAll().collectList().block().size();
        mlTaskType.setId(UUID.randomUUID());

        // Create the MlTaskType
        MlTaskTypeDTO mlTaskTypeDTO = mlTaskTypeMapper.toDto(mlTaskType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(mlTaskTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MlTaskType in the database
        List<MlTaskType> mlTaskTypeList = mlTaskTypeRepository.findAll().collectList().block();
        assertThat(mlTaskTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMlTaskTypeWithPatch() throws Exception {
        // Initialize the database
        mlTaskType.setId(UUID.randomUUID());
        mlTaskTypeRepository.save(mlTaskType).block();

        int databaseSizeBeforeUpdate = mlTaskTypeRepository.findAll().collectList().block().size();

        // Update the mlTaskType using partial update
        MlTaskType partialUpdatedMlTaskType = new MlTaskType();
        partialUpdatedMlTaskType.setId(mlTaskType.getId());

        partialUpdatedMlTaskType.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMlTaskType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMlTaskType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MlTaskType in the database
        List<MlTaskType> mlTaskTypeList = mlTaskTypeRepository.findAll().collectList().block();
        assertThat(mlTaskTypeList).hasSize(databaseSizeBeforeUpdate);
        MlTaskType testMlTaskType = mlTaskTypeList.get(mlTaskTypeList.size() - 1);
        assertThat(testMlTaskType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void fullUpdateMlTaskTypeWithPatch() throws Exception {
        // Initialize the database
        mlTaskType.setId(UUID.randomUUID());
        mlTaskTypeRepository.save(mlTaskType).block();

        int databaseSizeBeforeUpdate = mlTaskTypeRepository.findAll().collectList().block().size();

        // Update the mlTaskType using partial update
        MlTaskType partialUpdatedMlTaskType = new MlTaskType();
        partialUpdatedMlTaskType.setId(mlTaskType.getId());

        partialUpdatedMlTaskType.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMlTaskType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMlTaskType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the MlTaskType in the database
        List<MlTaskType> mlTaskTypeList = mlTaskTypeRepository.findAll().collectList().block();
        assertThat(mlTaskTypeList).hasSize(databaseSizeBeforeUpdate);
        MlTaskType testMlTaskType = mlTaskTypeList.get(mlTaskTypeList.size() - 1);
        assertThat(testMlTaskType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingMlTaskType() throws Exception {
        int databaseSizeBeforeUpdate = mlTaskTypeRepository.findAll().collectList().block().size();
        mlTaskType.setId(UUID.randomUUID());

        // Create the MlTaskType
        MlTaskTypeDTO mlTaskTypeDTO = mlTaskTypeMapper.toDto(mlTaskType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, mlTaskTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(mlTaskTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MlTaskType in the database
        List<MlTaskType> mlTaskTypeList = mlTaskTypeRepository.findAll().collectList().block();
        assertThat(mlTaskTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMlTaskType() throws Exception {
        int databaseSizeBeforeUpdate = mlTaskTypeRepository.findAll().collectList().block().size();
        mlTaskType.setId(UUID.randomUUID());

        // Create the MlTaskType
        MlTaskTypeDTO mlTaskTypeDTO = mlTaskTypeMapper.toDto(mlTaskType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(mlTaskTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the MlTaskType in the database
        List<MlTaskType> mlTaskTypeList = mlTaskTypeRepository.findAll().collectList().block();
        assertThat(mlTaskTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMlTaskType() throws Exception {
        int databaseSizeBeforeUpdate = mlTaskTypeRepository.findAll().collectList().block().size();
        mlTaskType.setId(UUID.randomUUID());

        // Create the MlTaskType
        MlTaskTypeDTO mlTaskTypeDTO = mlTaskTypeMapper.toDto(mlTaskType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(mlTaskTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the MlTaskType in the database
        List<MlTaskType> mlTaskTypeList = mlTaskTypeRepository.findAll().collectList().block();
        assertThat(mlTaskTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMlTaskType() {
        // Initialize the database
        mlTaskType.setId(UUID.randomUUID());
        mlTaskTypeRepository.save(mlTaskType).block();

        int databaseSizeBeforeDelete = mlTaskTypeRepository.findAll().collectList().block().size();

        // Delete the mlTaskType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, mlTaskType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<MlTaskType> mlTaskTypeList = mlTaskTypeRepository.findAll().collectList().block();
        assertThat(mlTaskTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
