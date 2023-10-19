package ai.turintech.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ai.turintech.catalog.IntegrationTest;
import ai.turintech.catalog.domain.ParameterType;
import ai.turintech.catalog.repository.EntityManager;
import ai.turintech.catalog.repository.ParameterTypeRepository;
import ai.turintech.catalog.service.dto.ParameterTypeDTO;
import ai.turintech.catalog.service.mapper.ParameterTypeMapper;
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
 * Integration tests for the {@link ParameterTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ParameterTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/parameter-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ParameterTypeRepository parameterTypeRepository;

    @Autowired
    private ParameterTypeMapper parameterTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ParameterType parameterType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParameterType createEntity(EntityManager em) {
        ParameterType parameterType = new ParameterType().id(UUID.randomUUID()).name(DEFAULT_NAME);
        return parameterType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParameterType createUpdatedEntity(EntityManager em) {
        ParameterType parameterType = new ParameterType().id(UUID.randomUUID()).name(UPDATED_NAME);
        return parameterType;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ParameterType.class).block();
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
        parameterType = createEntity(em);
    }

    @Test
    void createParameterType() throws Exception {
        int databaseSizeBeforeCreate = parameterTypeRepository.findAll().collectList().block().size();
        parameterType.setId(null);
        // Create the ParameterType
        ParameterTypeDTO parameterTypeDTO = parameterTypeMapper.toDto(parameterType);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ParameterType in the database
        List<ParameterType> parameterTypeList = parameterTypeRepository.findAll().collectList().block();
        assertThat(parameterTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ParameterType testParameterType = parameterTypeList.get(parameterTypeList.size() - 1);
        assertThat(testParameterType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void createParameterTypeWithExistingId() throws Exception {
        // Create the ParameterType with an existing ID
        parameterTypeRepository.save(parameterType).block();
        ParameterTypeDTO parameterTypeDTO = parameterTypeMapper.toDto(parameterType);

        int databaseSizeBeforeCreate = parameterTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParameterType in the database
        List<ParameterType> parameterTypeList = parameterTypeRepository.findAll().collectList().block();
        assertThat(parameterTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = parameterTypeRepository.findAll().collectList().block().size();
        // set the field null
        parameterType.setName(null);

        // Create the ParameterType, which fails.
        ParameterTypeDTO parameterTypeDTO = parameterTypeMapper.toDto(parameterType);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<ParameterType> parameterTypeList = parameterTypeRepository.findAll().collectList().block();
        assertThat(parameterTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllParameterTypesAsStream() {
        // Initialize the database
        parameterType.setId(UUID.randomUUID());
        parameterTypeRepository.save(parameterType).block();

        List<ParameterType> parameterTypeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ParameterTypeDTO.class)
            .getResponseBody()
            .map(parameterTypeMapper::toEntity)
            .filter(parameterType::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(parameterTypeList).isNotNull();
        assertThat(parameterTypeList).hasSize(1);
        ParameterType testParameterType = parameterTypeList.get(0);
        assertThat(testParameterType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    void getAllParameterTypes() {
        // Initialize the database
        parameterType.setId(UUID.randomUUID());
        parameterTypeRepository.save(parameterType).block();

        // Get all the parameterTypeList
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
            .value(hasItem(parameterType.getId().toString()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME));
    }

    @Test
    void getParameterType() {
        // Initialize the database
        parameterType.setId(UUID.randomUUID());
        parameterTypeRepository.save(parameterType).block();

        // Get the parameterType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, parameterType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(parameterType.getId().toString()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME));
    }

    @Test
    void getNonExistingParameterType() {
        // Get the parameterType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingParameterType() throws Exception {
        // Initialize the database
        parameterType.setId(UUID.randomUUID());
        parameterTypeRepository.save(parameterType).block();

        int databaseSizeBeforeUpdate = parameterTypeRepository.findAll().collectList().block().size();

        // Update the parameterType
        ParameterType updatedParameterType = parameterTypeRepository.findById(parameterType.getId()).block();
        updatedParameterType.name(UPDATED_NAME);
        ParameterTypeDTO parameterTypeDTO = parameterTypeMapper.toDto(updatedParameterType);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, parameterTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ParameterType in the database
        List<ParameterType> parameterTypeList = parameterTypeRepository.findAll().collectList().block();
        assertThat(parameterTypeList).hasSize(databaseSizeBeforeUpdate);
        ParameterType testParameterType = parameterTypeList.get(parameterTypeList.size() - 1);
        assertThat(testParameterType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void putNonExistingParameterType() throws Exception {
        int databaseSizeBeforeUpdate = parameterTypeRepository.findAll().collectList().block().size();
        parameterType.setId(UUID.randomUUID());

        // Create the ParameterType
        ParameterTypeDTO parameterTypeDTO = parameterTypeMapper.toDto(parameterType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, parameterTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParameterType in the database
        List<ParameterType> parameterTypeList = parameterTypeRepository.findAll().collectList().block();
        assertThat(parameterTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchParameterType() throws Exception {
        int databaseSizeBeforeUpdate = parameterTypeRepository.findAll().collectList().block().size();
        parameterType.setId(UUID.randomUUID());

        // Create the ParameterType
        ParameterTypeDTO parameterTypeDTO = parameterTypeMapper.toDto(parameterType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParameterType in the database
        List<ParameterType> parameterTypeList = parameterTypeRepository.findAll().collectList().block();
        assertThat(parameterTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamParameterType() throws Exception {
        int databaseSizeBeforeUpdate = parameterTypeRepository.findAll().collectList().block().size();
        parameterType.setId(UUID.randomUUID());

        // Create the ParameterType
        ParameterTypeDTO parameterTypeDTO = parameterTypeMapper.toDto(parameterType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ParameterType in the database
        List<ParameterType> parameterTypeList = parameterTypeRepository.findAll().collectList().block();
        assertThat(parameterTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateParameterTypeWithPatch() throws Exception {
        // Initialize the database
        parameterType.setId(UUID.randomUUID());
        parameterTypeRepository.save(parameterType).block();

        int databaseSizeBeforeUpdate = parameterTypeRepository.findAll().collectList().block().size();

        // Update the parameterType using partial update
        ParameterType partialUpdatedParameterType = new ParameterType();
        partialUpdatedParameterType.setId(parameterType.getId());

        partialUpdatedParameterType.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedParameterType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedParameterType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ParameterType in the database
        List<ParameterType> parameterTypeList = parameterTypeRepository.findAll().collectList().block();
        assertThat(parameterTypeList).hasSize(databaseSizeBeforeUpdate);
        ParameterType testParameterType = parameterTypeList.get(parameterTypeList.size() - 1);
        assertThat(testParameterType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void fullUpdateParameterTypeWithPatch() throws Exception {
        // Initialize the database
        parameterType.setId(UUID.randomUUID());
        parameterTypeRepository.save(parameterType).block();

        int databaseSizeBeforeUpdate = parameterTypeRepository.findAll().collectList().block().size();

        // Update the parameterType using partial update
        ParameterType partialUpdatedParameterType = new ParameterType();
        partialUpdatedParameterType.setId(parameterType.getId());

        partialUpdatedParameterType.name(UPDATED_NAME);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedParameterType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedParameterType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ParameterType in the database
        List<ParameterType> parameterTypeList = parameterTypeRepository.findAll().collectList().block();
        assertThat(parameterTypeList).hasSize(databaseSizeBeforeUpdate);
        ParameterType testParameterType = parameterTypeList.get(parameterTypeList.size() - 1);
        assertThat(testParameterType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    void patchNonExistingParameterType() throws Exception {
        int databaseSizeBeforeUpdate = parameterTypeRepository.findAll().collectList().block().size();
        parameterType.setId(UUID.randomUUID());

        // Create the ParameterType
        ParameterTypeDTO parameterTypeDTO = parameterTypeMapper.toDto(parameterType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, parameterTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParameterType in the database
        List<ParameterType> parameterTypeList = parameterTypeRepository.findAll().collectList().block();
        assertThat(parameterTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchParameterType() throws Exception {
        int databaseSizeBeforeUpdate = parameterTypeRepository.findAll().collectList().block().size();
        parameterType.setId(UUID.randomUUID());

        // Create the ParameterType
        ParameterTypeDTO parameterTypeDTO = parameterTypeMapper.toDto(parameterType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ParameterType in the database
        List<ParameterType> parameterTypeList = parameterTypeRepository.findAll().collectList().block();
        assertThat(parameterTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamParameterType() throws Exception {
        int databaseSizeBeforeUpdate = parameterTypeRepository.findAll().collectList().block().size();
        parameterType.setId(UUID.randomUUID());

        // Create the ParameterType
        ParameterTypeDTO parameterTypeDTO = parameterTypeMapper.toDto(parameterType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(parameterTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ParameterType in the database
        List<ParameterType> parameterTypeList = parameterTypeRepository.findAll().collectList().block();
        assertThat(parameterTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteParameterType() {
        // Initialize the database
        parameterType.setId(UUID.randomUUID());
        parameterTypeRepository.save(parameterType).block();

        int databaseSizeBeforeDelete = parameterTypeRepository.findAll().collectList().block().size();

        // Delete the parameterType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, parameterType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ParameterType> parameterTypeList = parameterTypeRepository.findAll().collectList().block();
        assertThat(parameterTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
