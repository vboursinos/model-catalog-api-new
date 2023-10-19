package ai.turintech.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ai.turintech.catalog.IntegrationTest;
import ai.turintech.catalog.domain.CategoricalParameter;
import ai.turintech.catalog.repository.CategoricalParameterRepository;
import ai.turintech.catalog.repository.EntityManager;
import ai.turintech.catalog.service.dto.CategoricalParameterDTO;
import ai.turintech.catalog.service.mapper.CategoricalParameterMapper;
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
 * Integration tests for the {@link CategoricalParameterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CategoricalParameterResourceIT {

    private static final String DEFAULT_DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/categorical-parameters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoricalParameterRepository categoricalParameterRepository;

    @Autowired
    private CategoricalParameterMapper categoricalParameterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CategoricalParameter categoricalParameter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoricalParameter createEntity(EntityManager em) {
        CategoricalParameter categoricalParameter = new CategoricalParameter().defaultValue(DEFAULT_DEFAULT_VALUE);
        return categoricalParameter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoricalParameter createUpdatedEntity(EntityManager em) {
        CategoricalParameter categoricalParameter = new CategoricalParameter().defaultValue(UPDATED_DEFAULT_VALUE);
        return categoricalParameter;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CategoricalParameter.class).block();
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
        categoricalParameter = createEntity(em);
    }

    @Test
    void createCategoricalParameter() throws Exception {
        int databaseSizeBeforeCreate = categoricalParameterRepository.findAll().collectList().block().size();
        // Create the CategoricalParameter
        CategoricalParameterDTO categoricalParameterDTO = categoricalParameterMapper.toDto(categoricalParameter);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoricalParameterDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CategoricalParameter in the database
        List<CategoricalParameter> categoricalParameterList = categoricalParameterRepository.findAll().collectList().block();
        assertThat(categoricalParameterList).hasSize(databaseSizeBeforeCreate + 1);
        CategoricalParameter testCategoricalParameter = categoricalParameterList.get(categoricalParameterList.size() - 1);
        assertThat(testCategoricalParameter.getDefaultValue()).isEqualTo(DEFAULT_DEFAULT_VALUE);
    }

    @Test
    void createCategoricalParameterWithExistingId() throws Exception {
        // Create the CategoricalParameter with an existing ID
        categoricalParameter.setId(1L);
        CategoricalParameterDTO categoricalParameterDTO = categoricalParameterMapper.toDto(categoricalParameter);

        int databaseSizeBeforeCreate = categoricalParameterRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoricalParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CategoricalParameter in the database
        List<CategoricalParameter> categoricalParameterList = categoricalParameterRepository.findAll().collectList().block();
        assertThat(categoricalParameterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCategoricalParametersAsStream() {
        // Initialize the database
        categoricalParameterRepository.save(categoricalParameter).block();

        List<CategoricalParameter> categoricalParameterList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CategoricalParameterDTO.class)
            .getResponseBody()
            .map(categoricalParameterMapper::toEntity)
            .filter(categoricalParameter::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(categoricalParameterList).isNotNull();
        assertThat(categoricalParameterList).hasSize(1);
        CategoricalParameter testCategoricalParameter = categoricalParameterList.get(0);
        assertThat(testCategoricalParameter.getDefaultValue()).isEqualTo(DEFAULT_DEFAULT_VALUE);
    }

    @Test
    void getAllCategoricalParameters() {
        // Initialize the database
        categoricalParameterRepository.save(categoricalParameter).block();

        // Get all the categoricalParameterList
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
            .value(hasItem(categoricalParameter.getId().intValue()))
            .jsonPath("$.[*].defaultValue")
            .value(hasItem(DEFAULT_DEFAULT_VALUE));
    }

    @Test
    void getCategoricalParameter() {
        // Initialize the database
        categoricalParameterRepository.save(categoricalParameter).block();

        // Get the categoricalParameter
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, categoricalParameter.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(categoricalParameter.getId().intValue()))
            .jsonPath("$.defaultValue")
            .value(is(DEFAULT_DEFAULT_VALUE));
    }

    @Test
    void getNonExistingCategoricalParameter() {
        // Get the categoricalParameter
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCategoricalParameter() throws Exception {
        // Initialize the database
        categoricalParameterRepository.save(categoricalParameter).block();

        int databaseSizeBeforeUpdate = categoricalParameterRepository.findAll().collectList().block().size();

        // Update the categoricalParameter
        CategoricalParameter updatedCategoricalParameter = categoricalParameterRepository.findById(categoricalParameter.getId()).block();
        updatedCategoricalParameter.defaultValue(UPDATED_DEFAULT_VALUE);
        CategoricalParameterDTO categoricalParameterDTO = categoricalParameterMapper.toDto(updatedCategoricalParameter);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, categoricalParameterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoricalParameterDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CategoricalParameter in the database
        List<CategoricalParameter> categoricalParameterList = categoricalParameterRepository.findAll().collectList().block();
        assertThat(categoricalParameterList).hasSize(databaseSizeBeforeUpdate);
        CategoricalParameter testCategoricalParameter = categoricalParameterList.get(categoricalParameterList.size() - 1);
        assertThat(testCategoricalParameter.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
    }

    @Test
    void putNonExistingCategoricalParameter() throws Exception {
        int databaseSizeBeforeUpdate = categoricalParameterRepository.findAll().collectList().block().size();
        categoricalParameter.setId(count.incrementAndGet());

        // Create the CategoricalParameter
        CategoricalParameterDTO categoricalParameterDTO = categoricalParameterMapper.toDto(categoricalParameter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, categoricalParameterDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoricalParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CategoricalParameter in the database
        List<CategoricalParameter> categoricalParameterList = categoricalParameterRepository.findAll().collectList().block();
        assertThat(categoricalParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCategoricalParameter() throws Exception {
        int databaseSizeBeforeUpdate = categoricalParameterRepository.findAll().collectList().block().size();
        categoricalParameter.setId(count.incrementAndGet());

        // Create the CategoricalParameter
        CategoricalParameterDTO categoricalParameterDTO = categoricalParameterMapper.toDto(categoricalParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoricalParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CategoricalParameter in the database
        List<CategoricalParameter> categoricalParameterList = categoricalParameterRepository.findAll().collectList().block();
        assertThat(categoricalParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCategoricalParameter() throws Exception {
        int databaseSizeBeforeUpdate = categoricalParameterRepository.findAll().collectList().block().size();
        categoricalParameter.setId(count.incrementAndGet());

        // Create the CategoricalParameter
        CategoricalParameterDTO categoricalParameterDTO = categoricalParameterMapper.toDto(categoricalParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoricalParameterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CategoricalParameter in the database
        List<CategoricalParameter> categoricalParameterList = categoricalParameterRepository.findAll().collectList().block();
        assertThat(categoricalParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCategoricalParameterWithPatch() throws Exception {
        // Initialize the database
        categoricalParameterRepository.save(categoricalParameter).block();

        int databaseSizeBeforeUpdate = categoricalParameterRepository.findAll().collectList().block().size();

        // Update the categoricalParameter using partial update
        CategoricalParameter partialUpdatedCategoricalParameter = new CategoricalParameter();
        partialUpdatedCategoricalParameter.setId(categoricalParameter.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCategoricalParameter.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoricalParameter))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CategoricalParameter in the database
        List<CategoricalParameter> categoricalParameterList = categoricalParameterRepository.findAll().collectList().block();
        assertThat(categoricalParameterList).hasSize(databaseSizeBeforeUpdate);
        CategoricalParameter testCategoricalParameter = categoricalParameterList.get(categoricalParameterList.size() - 1);
        assertThat(testCategoricalParameter.getDefaultValue()).isEqualTo(DEFAULT_DEFAULT_VALUE);
    }

    @Test
    void fullUpdateCategoricalParameterWithPatch() throws Exception {
        // Initialize the database
        categoricalParameterRepository.save(categoricalParameter).block();

        int databaseSizeBeforeUpdate = categoricalParameterRepository.findAll().collectList().block().size();

        // Update the categoricalParameter using partial update
        CategoricalParameter partialUpdatedCategoricalParameter = new CategoricalParameter();
        partialUpdatedCategoricalParameter.setId(categoricalParameter.getId());

        partialUpdatedCategoricalParameter.defaultValue(UPDATED_DEFAULT_VALUE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCategoricalParameter.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoricalParameter))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CategoricalParameter in the database
        List<CategoricalParameter> categoricalParameterList = categoricalParameterRepository.findAll().collectList().block();
        assertThat(categoricalParameterList).hasSize(databaseSizeBeforeUpdate);
        CategoricalParameter testCategoricalParameter = categoricalParameterList.get(categoricalParameterList.size() - 1);
        assertThat(testCategoricalParameter.getDefaultValue()).isEqualTo(UPDATED_DEFAULT_VALUE);
    }

    @Test
    void patchNonExistingCategoricalParameter() throws Exception {
        int databaseSizeBeforeUpdate = categoricalParameterRepository.findAll().collectList().block().size();
        categoricalParameter.setId(count.incrementAndGet());

        // Create the CategoricalParameter
        CategoricalParameterDTO categoricalParameterDTO = categoricalParameterMapper.toDto(categoricalParameter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, categoricalParameterDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoricalParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CategoricalParameter in the database
        List<CategoricalParameter> categoricalParameterList = categoricalParameterRepository.findAll().collectList().block();
        assertThat(categoricalParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCategoricalParameter() throws Exception {
        int databaseSizeBeforeUpdate = categoricalParameterRepository.findAll().collectList().block().size();
        categoricalParameter.setId(count.incrementAndGet());

        // Create the CategoricalParameter
        CategoricalParameterDTO categoricalParameterDTO = categoricalParameterMapper.toDto(categoricalParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoricalParameterDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CategoricalParameter in the database
        List<CategoricalParameter> categoricalParameterList = categoricalParameterRepository.findAll().collectList().block();
        assertThat(categoricalParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCategoricalParameter() throws Exception {
        int databaseSizeBeforeUpdate = categoricalParameterRepository.findAll().collectList().block().size();
        categoricalParameter.setId(count.incrementAndGet());

        // Create the CategoricalParameter
        CategoricalParameterDTO categoricalParameterDTO = categoricalParameterMapper.toDto(categoricalParameter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(categoricalParameterDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CategoricalParameter in the database
        List<CategoricalParameter> categoricalParameterList = categoricalParameterRepository.findAll().collectList().block();
        assertThat(categoricalParameterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCategoricalParameter() {
        // Initialize the database
        categoricalParameterRepository.save(categoricalParameter).block();

        int databaseSizeBeforeDelete = categoricalParameterRepository.findAll().collectList().block().size();

        // Delete the categoricalParameter
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, categoricalParameter.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CategoricalParameter> categoricalParameterList = categoricalParameterRepository.findAll().collectList().block();
        assertThat(categoricalParameterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
