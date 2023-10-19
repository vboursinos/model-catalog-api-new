package ai.turintech.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ai.turintech.catalog.IntegrationTest;
import ai.turintech.catalog.domain.CategoricalParameterValue;
import ai.turintech.catalog.repository.CategoricalParameterValueRepository;
import ai.turintech.catalog.repository.EntityManager;
import ai.turintech.catalog.service.dto.CategoricalParameterValueDTO;
import ai.turintech.catalog.service.mapper.CategoricalParameterValueMapper;
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
 * Integration tests for the {@link CategoricalParameterValueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CategoricalParameterValueResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/categorical-parameter-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoricalParameterValueRepository categoricalParameterValueRepository;

    @Autowired
    private CategoricalParameterValueMapper categoricalParameterValueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CategoricalParameterValue categoricalParameterValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoricalParameterValue createEntity(EntityManager em) {
        CategoricalParameterValue categoricalParameterValue = new CategoricalParameterValue().value(DEFAULT_VALUE);
        return categoricalParameterValue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoricalParameterValue createUpdatedEntity(EntityManager em) {
        CategoricalParameterValue categoricalParameterValue = new CategoricalParameterValue().value(UPDATED_VALUE);
        return categoricalParameterValue;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CategoricalParameterValue.class).block();
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
        categoricalParameterValue = createEntity(em);
    }

    @Test
    void createCategoricalParameterValue() throws Exception {
        int databaseSizeBeforeCreate = categoricalParameterValueRepository.findAll().collectList().block().size();
        // Create the CategoricalParameterValue
        CategoricalParameterValueDTO categoricalParameterValueDTO = categoricalParameterValueMapper.toDto(categoricalParameterValue);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoricalParameterValueDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CategoricalParameterValue in the database
        List<CategoricalParameterValue> categoricalParameterValueList = categoricalParameterValueRepository.findAll().collectList().block();
        assertThat(categoricalParameterValueList).hasSize(databaseSizeBeforeCreate + 1);
        CategoricalParameterValue testCategoricalParameterValue = categoricalParameterValueList.get(
            categoricalParameterValueList.size() - 1
        );
        assertThat(testCategoricalParameterValue.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    void createCategoricalParameterValueWithExistingId() throws Exception {
        // Create the CategoricalParameterValue with an existing ID
        categoricalParameterValue.setId(1L);
        CategoricalParameterValueDTO categoricalParameterValueDTO = categoricalParameterValueMapper.toDto(categoricalParameterValue);

        int databaseSizeBeforeCreate = categoricalParameterValueRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoricalParameterValueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CategoricalParameterValue in the database
        List<CategoricalParameterValue> categoricalParameterValueList = categoricalParameterValueRepository.findAll().collectList().block();
        assertThat(categoricalParameterValueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoricalParameterValueRepository.findAll().collectList().block().size();
        // set the field null
        categoricalParameterValue.setValue(null);

        // Create the CategoricalParameterValue, which fails.
        CategoricalParameterValueDTO categoricalParameterValueDTO = categoricalParameterValueMapper.toDto(categoricalParameterValue);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoricalParameterValueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CategoricalParameterValue> categoricalParameterValueList = categoricalParameterValueRepository.findAll().collectList().block();
        assertThat(categoricalParameterValueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCategoricalParameterValuesAsStream() {
        // Initialize the database
        categoricalParameterValueRepository.save(categoricalParameterValue).block();

        List<CategoricalParameterValue> categoricalParameterValueList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CategoricalParameterValueDTO.class)
            .getResponseBody()
            .map(categoricalParameterValueMapper::toEntity)
            .filter(categoricalParameterValue::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(categoricalParameterValueList).isNotNull();
        assertThat(categoricalParameterValueList).hasSize(1);
        CategoricalParameterValue testCategoricalParameterValue = categoricalParameterValueList.get(0);
        assertThat(testCategoricalParameterValue.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    void getAllCategoricalParameterValues() {
        // Initialize the database
        categoricalParameterValueRepository.save(categoricalParameterValue).block();

        // Get all the categoricalParameterValueList
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
            .value(hasItem(categoricalParameterValue.getId().intValue()))
            .jsonPath("$.[*].value")
            .value(hasItem(DEFAULT_VALUE));
    }

    @Test
    void getCategoricalParameterValue() {
        // Initialize the database
        categoricalParameterValueRepository.save(categoricalParameterValue).block();

        // Get the categoricalParameterValue
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, categoricalParameterValue.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(categoricalParameterValue.getId().intValue()))
            .jsonPath("$.value")
            .value(is(DEFAULT_VALUE));
    }

    @Test
    void getNonExistingCategoricalParameterValue() {
        // Get the categoricalParameterValue
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCategoricalParameterValue() throws Exception {
        // Initialize the database
        categoricalParameterValueRepository.save(categoricalParameterValue).block();

        int databaseSizeBeforeUpdate = categoricalParameterValueRepository.findAll().collectList().block().size();

        // Update the categoricalParameterValue
        CategoricalParameterValue updatedCategoricalParameterValue = categoricalParameterValueRepository
            .findById(categoricalParameterValue.getId())
            .block();
        updatedCategoricalParameterValue.value(UPDATED_VALUE);
        CategoricalParameterValueDTO categoricalParameterValueDTO = categoricalParameterValueMapper.toDto(updatedCategoricalParameterValue);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, categoricalParameterValueDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoricalParameterValueDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CategoricalParameterValue in the database
        List<CategoricalParameterValue> categoricalParameterValueList = categoricalParameterValueRepository.findAll().collectList().block();
        assertThat(categoricalParameterValueList).hasSize(databaseSizeBeforeUpdate);
        CategoricalParameterValue testCategoricalParameterValue = categoricalParameterValueList.get(
            categoricalParameterValueList.size() - 1
        );
        assertThat(testCategoricalParameterValue.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void putNonExistingCategoricalParameterValue() throws Exception {
        int databaseSizeBeforeUpdate = categoricalParameterValueRepository.findAll().collectList().block().size();
        categoricalParameterValue.setId(count.incrementAndGet());

        // Create the CategoricalParameterValue
        CategoricalParameterValueDTO categoricalParameterValueDTO = categoricalParameterValueMapper.toDto(categoricalParameterValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, categoricalParameterValueDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoricalParameterValueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CategoricalParameterValue in the database
        List<CategoricalParameterValue> categoricalParameterValueList = categoricalParameterValueRepository.findAll().collectList().block();
        assertThat(categoricalParameterValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCategoricalParameterValue() throws Exception {
        int databaseSizeBeforeUpdate = categoricalParameterValueRepository.findAll().collectList().block().size();
        categoricalParameterValue.setId(count.incrementAndGet());

        // Create the CategoricalParameterValue
        CategoricalParameterValueDTO categoricalParameterValueDTO = categoricalParameterValueMapper.toDto(categoricalParameterValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoricalParameterValueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CategoricalParameterValue in the database
        List<CategoricalParameterValue> categoricalParameterValueList = categoricalParameterValueRepository.findAll().collectList().block();
        assertThat(categoricalParameterValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCategoricalParameterValue() throws Exception {
        int databaseSizeBeforeUpdate = categoricalParameterValueRepository.findAll().collectList().block().size();
        categoricalParameterValue.setId(count.incrementAndGet());

        // Create the CategoricalParameterValue
        CategoricalParameterValueDTO categoricalParameterValueDTO = categoricalParameterValueMapper.toDto(categoricalParameterValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoricalParameterValueDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CategoricalParameterValue in the database
        List<CategoricalParameterValue> categoricalParameterValueList = categoricalParameterValueRepository.findAll().collectList().block();
        assertThat(categoricalParameterValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCategoricalParameterValueWithPatch() throws Exception {
        // Initialize the database
        categoricalParameterValueRepository.save(categoricalParameterValue).block();

        int databaseSizeBeforeUpdate = categoricalParameterValueRepository.findAll().collectList().block().size();

        // Update the categoricalParameterValue using partial update
        CategoricalParameterValue partialUpdatedCategoricalParameterValue = new CategoricalParameterValue();
        partialUpdatedCategoricalParameterValue.setId(categoricalParameterValue.getId());

        partialUpdatedCategoricalParameterValue.value(UPDATED_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCategoricalParameterValue.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoricalParameterValue))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CategoricalParameterValue in the database
        List<CategoricalParameterValue> categoricalParameterValueList = categoricalParameterValueRepository.findAll().collectList().block();
        assertThat(categoricalParameterValueList).hasSize(databaseSizeBeforeUpdate);
        CategoricalParameterValue testCategoricalParameterValue = categoricalParameterValueList.get(
            categoricalParameterValueList.size() - 1
        );
        assertThat(testCategoricalParameterValue.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void fullUpdateCategoricalParameterValueWithPatch() throws Exception {
        // Initialize the database
        categoricalParameterValueRepository.save(categoricalParameterValue).block();

        int databaseSizeBeforeUpdate = categoricalParameterValueRepository.findAll().collectList().block().size();

        // Update the categoricalParameterValue using partial update
        CategoricalParameterValue partialUpdatedCategoricalParameterValue = new CategoricalParameterValue();
        partialUpdatedCategoricalParameterValue.setId(categoricalParameterValue.getId());

        partialUpdatedCategoricalParameterValue.value(UPDATED_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCategoricalParameterValue.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoricalParameterValue))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CategoricalParameterValue in the database
        List<CategoricalParameterValue> categoricalParameterValueList = categoricalParameterValueRepository.findAll().collectList().block();
        assertThat(categoricalParameterValueList).hasSize(databaseSizeBeforeUpdate);
        CategoricalParameterValue testCategoricalParameterValue = categoricalParameterValueList.get(
            categoricalParameterValueList.size() - 1
        );
        assertThat(testCategoricalParameterValue.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    void patchNonExistingCategoricalParameterValue() throws Exception {
        int databaseSizeBeforeUpdate = categoricalParameterValueRepository.findAll().collectList().block().size();
        categoricalParameterValue.setId(count.incrementAndGet());

        // Create the CategoricalParameterValue
        CategoricalParameterValueDTO categoricalParameterValueDTO = categoricalParameterValueMapper.toDto(categoricalParameterValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, categoricalParameterValueDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoricalParameterValueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CategoricalParameterValue in the database
        List<CategoricalParameterValue> categoricalParameterValueList = categoricalParameterValueRepository.findAll().collectList().block();
        assertThat(categoricalParameterValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCategoricalParameterValue() throws Exception {
        int databaseSizeBeforeUpdate = categoricalParameterValueRepository.findAll().collectList().block().size();
        categoricalParameterValue.setId(count.incrementAndGet());

        // Create the CategoricalParameterValue
        CategoricalParameterValueDTO categoricalParameterValueDTO = categoricalParameterValueMapper.toDto(categoricalParameterValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoricalParameterValueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CategoricalParameterValue in the database
        List<CategoricalParameterValue> categoricalParameterValueList = categoricalParameterValueRepository.findAll().collectList().block();
        assertThat(categoricalParameterValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCategoricalParameterValue() throws Exception {
        int databaseSizeBeforeUpdate = categoricalParameterValueRepository.findAll().collectList().block().size();
        categoricalParameterValue.setId(count.incrementAndGet());

        // Create the CategoricalParameterValue
        CategoricalParameterValueDTO categoricalParameterValueDTO = categoricalParameterValueMapper.toDto(categoricalParameterValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoricalParameterValueDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CategoricalParameterValue in the database
        List<CategoricalParameterValue> categoricalParameterValueList = categoricalParameterValueRepository.findAll().collectList().block();
        assertThat(categoricalParameterValueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCategoricalParameterValue() {
        // Initialize the database
        categoricalParameterValueRepository.save(categoricalParameterValue).block();

        int databaseSizeBeforeDelete = categoricalParameterValueRepository.findAll().collectList().block().size();

        // Delete the categoricalParameterValue
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, categoricalParameterValue.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CategoricalParameterValue> categoricalParameterValueList = categoricalParameterValueRepository.findAll().collectList().block();
        assertThat(categoricalParameterValueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
