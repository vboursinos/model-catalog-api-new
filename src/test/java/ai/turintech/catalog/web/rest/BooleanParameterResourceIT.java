package ai.turintech.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ai.turintech.catalog.IntegrationTest;
import ai.turintech.catalog.domain.BooleanParameter;
import ai.turintech.catalog.repository.BooleanParameterRepository;
import ai.turintech.catalog.repository.EntityManager;
import ai.turintech.catalog.service.dto.BooleanParameterDTO;
import ai.turintech.catalog.service.mapper.BooleanParameterMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link BooleanParameterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class BooleanParameterResourceIT {

    private static final Boolean DEFAULT_DEFAULT_VALUE = false;
    private static final Boolean UPDATED_DEFAULT_VALUE = true;

    private static final String ENTITY_API_URL = "/api/boolean-parameters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BooleanParameterRepository booleanParameterRepository;

    @Autowired
    private BooleanParameterMapper booleanParameterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private BooleanParameter booleanParameter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BooleanParameter createEntity(EntityManager em) {
        BooleanParameter booleanParameter = new BooleanParameter().defaultValue(DEFAULT_DEFAULT_VALUE);
        return booleanParameter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BooleanParameter createUpdatedEntity(EntityManager em) {
        BooleanParameter booleanParameter = new BooleanParameter().defaultValue(UPDATED_DEFAULT_VALUE);
        return booleanParameter;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(BooleanParameter.class).block();
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
        booleanParameter = createEntity(em);
    }

    @Test
    void createBooleanParameter() throws Exception {
        int databaseSizeBeforeCreate = booleanParameterRepository.findAll().collectList().block().size();
        // Create the BooleanParameter
        BooleanParameterDTO booleanParameterDTO = booleanParameterMapper.toDto(booleanParameter);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(booleanParameterDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the BooleanParameter in the database
        List<BooleanParameter> booleanParameterList = booleanParameterRepository.findAll().collectList().block();
        assertThat(booleanParameterList).hasSize(databaseSizeBeforeCreate + 1);
        BooleanParameter testBooleanParameter = booleanParameterList.get(booleanParameterList.size() - 1);
        assertThat(testBooleanParameter.getDefaultValue()).isEqualTo(DEFAULT_DEFAULT_VALUE);
    }

    @Test
    void createBooleanParameterWithExistingId() throws Exception {
        // Create the BooleanParameter with an existing ID
        booleanParameter.setId(1L);
        BooleanParameterDTO booleanParameterDTO = booleanParameterMapper.toDto(booleanParameter);

        int databaseSizeBeforeCreate = booleanParameterRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(booleanParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BooleanParameter in the database
        List<BooleanParameter> booleanParameterList = booleanParameterRepository.findAll().collectList().block();
        assertThat(booleanParameterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllBooleanParametersAsStream() {
        // Initialize the database
        booleanParameterRepository.save(booleanParameter).block();

        List<BooleanParameter> booleanParameterList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(BooleanParameterDTO.class)
            .getResponseBody()
            .map(booleanParameterMapper::toEntity)
            .filter(booleanParameter::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(booleanParameterList).isNotNull();
        assertThat(booleanParameterList).hasSize(1);
        BooleanParameter testBooleanParameter = booleanParameterList.get(0);
        assertThat(testBooleanParameter.getDefaultValue()).isEqualTo(DEFAULT_DEFAULT_VALUE);
    }

    @Test
    void getAllBooleanParameters() {
        // Initialize the database
        booleanParameterRepository.save(booleanParameter).block();

        // Get all the booleanParameterList
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
            .value(hasItem(booleanParameter.getId().intValue()))
            .jsonPath("$.[*].defaultValue")
            .value(hasItem(DEFAULT_DEFAULT_VALUE.booleanValue()));
    }

    @Test
    void getBooleanParameter() {
        // Initialize the database
        booleanParameterRepository.save(booleanParameter).block();

        // Get the booleanParameter
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, booleanParameter.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(booleanParameter.getId().intValue()))
            .jsonPath("$.defaultValue")
            .value(is(DEFAULT_DEFAULT_VALUE.booleanValue()));
    }

    @Test
    void getNonExistingBooleanParameter() {
        // Get the booleanParameter
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingBooleanParameter() throws Exception {
        // Initialize the database
        booleanParameterRepository.save(booleanParameter).block();

        int databaseSizeBeforeUpdate = booleanParameterRepository.findAll().collectList().block().size();

        // Update the booleanParameter
        BooleanParameter updatedBooleanParameter = booleanParameterRepository.findById(booleanParameter.getId()).block();
        updatedBooleanParameter.defaultValue(UPDATED_DEFAULT_VALUE);
        BooleanParameterDTO booleanParameterDTO = booleanParameterMapper.toDto(updatedBooleanParameter);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, booleanParameterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(booleanParameterDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BooleanParameter in the database
        List<BooleanParameter> booleanParameterList = booleanParameterRepository.findAll().collectList().block();
        assertThat(booleanParameterList).hasSize(databaseSizeBeforeUpdate);
        BooleanParameter testBooleanParameter = booleanParameterList.get(booleanParameterList.size() - 1);
        assertThat(testBooleanParameter.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
    }

    @Test
    void putNonExistingBooleanParameter() throws Exception {
        int databaseSizeBeforeUpdate = booleanParameterRepository.findAll().collectList().block().size();
        booleanParameter.setId(count.incrementAndGet());

        // Create the BooleanParameter
        BooleanParameterDTO booleanParameterDTO = booleanParameterMapper.toDto(booleanParameter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, booleanParameterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(booleanParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BooleanParameter in the database
        List<BooleanParameter> booleanParameterList = booleanParameterRepository.findAll().collectList().block();
        assertThat(booleanParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBooleanParameter() throws Exception {
        int databaseSizeBeforeUpdate = booleanParameterRepository.findAll().collectList().block().size();
        booleanParameter.setId(count.incrementAndGet());

        // Create the BooleanParameter
        BooleanParameterDTO booleanParameterDTO = booleanParameterMapper.toDto(booleanParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(booleanParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BooleanParameter in the database
        List<BooleanParameter> booleanParameterList = booleanParameterRepository.findAll().collectList().block();
        assertThat(booleanParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBooleanParameter() throws Exception {
        int databaseSizeBeforeUpdate = booleanParameterRepository.findAll().collectList().block().size();
        booleanParameter.setId(count.incrementAndGet());

        // Create the BooleanParameter
        BooleanParameterDTO booleanParameterDTO = booleanParameterMapper.toDto(booleanParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(booleanParameterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the BooleanParameter in the database
        List<BooleanParameter> booleanParameterList = booleanParameterRepository.findAll().collectList().block();
        assertThat(booleanParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBooleanParameterWithPatch() throws Exception {
        // Initialize the database
        booleanParameterRepository.save(booleanParameter).block();

        int databaseSizeBeforeUpdate = booleanParameterRepository.findAll().collectList().block().size();

        // Update the booleanParameter using partial update
        BooleanParameter partialUpdatedBooleanParameter = new BooleanParameter();
        partialUpdatedBooleanParameter.setId(booleanParameter.getId());

        partialUpdatedBooleanParameter.defaultValue(UPDATED_DEFAULT_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBooleanParameter.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBooleanParameter))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BooleanParameter in the database
        List<BooleanParameter> booleanParameterList = booleanParameterRepository.findAll().collectList().block();
        assertThat(booleanParameterList).hasSize(databaseSizeBeforeUpdate);
        BooleanParameter testBooleanParameter = booleanParameterList.get(booleanParameterList.size() - 1);
        assertThat(testBooleanParameter.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
    }

    @Test
    void fullUpdateBooleanParameterWithPatch() throws Exception {
        // Initialize the database
        booleanParameterRepository.save(booleanParameter).block();

        int databaseSizeBeforeUpdate = booleanParameterRepository.findAll().collectList().block().size();

        // Update the booleanParameter using partial update
        BooleanParameter partialUpdatedBooleanParameter = new BooleanParameter();
        partialUpdatedBooleanParameter.setId(booleanParameter.getId());

        partialUpdatedBooleanParameter.defaultValue(UPDATED_DEFAULT_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBooleanParameter.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBooleanParameter))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the BooleanParameter in the database
        List<BooleanParameter> booleanParameterList = booleanParameterRepository.findAll().collectList().block();
        assertThat(booleanParameterList).hasSize(databaseSizeBeforeUpdate);
        BooleanParameter testBooleanParameter = booleanParameterList.get(booleanParameterList.size() - 1);
        assertThat(testBooleanParameter.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
    }

    @Test
    void patchNonExistingBooleanParameter() throws Exception {
        int databaseSizeBeforeUpdate = booleanParameterRepository.findAll().collectList().block().size();
        booleanParameter.setId(count.incrementAndGet());

        // Create the BooleanParameter
        BooleanParameterDTO booleanParameterDTO = booleanParameterMapper.toDto(booleanParameter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, booleanParameterDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(booleanParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BooleanParameter in the database
        List<BooleanParameter> booleanParameterList = booleanParameterRepository.findAll().collectList().block();
        assertThat(booleanParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBooleanParameter() throws Exception {
        int databaseSizeBeforeUpdate = booleanParameterRepository.findAll().collectList().block().size();
        booleanParameter.setId(count.incrementAndGet());

        // Create the BooleanParameter
        BooleanParameterDTO booleanParameterDTO = booleanParameterMapper.toDto(booleanParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(booleanParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the BooleanParameter in the database
        List<BooleanParameter> booleanParameterList = booleanParameterRepository.findAll().collectList().block();
        assertThat(booleanParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBooleanParameter() throws Exception {
        int databaseSizeBeforeUpdate = booleanParameterRepository.findAll().collectList().block().size();
        booleanParameter.setId(count.incrementAndGet());

        // Create the BooleanParameter
        BooleanParameterDTO booleanParameterDTO = booleanParameterMapper.toDto(booleanParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(booleanParameterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the BooleanParameter in the database
        List<BooleanParameter> booleanParameterList = booleanParameterRepository.findAll().collectList().block();
        assertThat(booleanParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBooleanParameter() {
        // Initialize the database
        booleanParameterRepository.save(booleanParameter).block();

        int databaseSizeBeforeDelete = booleanParameterRepository.findAll().collectList().block().size();

        // Delete the booleanParameter
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, booleanParameter.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<BooleanParameter> booleanParameterList = booleanParameterRepository.findAll().collectList().block();
        assertThat(booleanParameterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
