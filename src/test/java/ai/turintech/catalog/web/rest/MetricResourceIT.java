package ai.turintech.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import ai.turintech.catalog.IntegrationTest;
import ai.turintech.catalog.domain.Metric;
import ai.turintech.catalog.repository.EntityManager;
import ai.turintech.catalog.repository.MetricRepository;
import ai.turintech.catalog.service.dto.MetricDTO;
import ai.turintech.catalog.service.mapper.MetricMapper;
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
 * Integration tests for the {@link MetricResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MetricResourceIT {

    private static final String DEFAULT_METRIC = "AAAAAAAAAA";
    private static final String UPDATED_METRIC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/metrics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private MetricRepository metricRepository;

    @Autowired
    private MetricMapper metricMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Metric metric;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Metric createEntity(EntityManager em) {
        Metric metric = new Metric().id(UUID.randomUUID()).metric(DEFAULT_METRIC);
        return metric;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Metric createUpdatedEntity(EntityManager em) {
        Metric metric = new Metric().id(UUID.randomUUID()).metric(UPDATED_METRIC);
        return metric;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Metric.class).block();
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
        metric = createEntity(em);
    }

    @Test
    void createMetric() throws Exception {
        int databaseSizeBeforeCreate = metricRepository.findAll().collectList().block().size();
        metric.setId(null);
        // Create the Metric
        MetricDTO metricDTO = metricMapper.toDto(metric);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(metricDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Metric in the database
        List<Metric> metricList = metricRepository.findAll().collectList().block();
        assertThat(metricList).hasSize(databaseSizeBeforeCreate + 1);
        Metric testMetric = metricList.get(metricList.size() - 1);
        assertThat(testMetric.getMetric()).isEqualTo(DEFAULT_METRIC);
    }

    @Test
    void createMetricWithExistingId() throws Exception {
        // Create the Metric with an existing ID
        metricRepository.save(metric).block();
        MetricDTO metricDTO = metricMapper.toDto(metric);

        int databaseSizeBeforeCreate = metricRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(metricDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Metric in the database
        List<Metric> metricList = metricRepository.findAll().collectList().block();
        assertThat(metricList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllMetricsAsStream() {
        // Initialize the database
        metric.setId(UUID.randomUUID());
        metricRepository.save(metric).block();

        List<Metric> metricList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(MetricDTO.class)
            .getResponseBody()
            .map(metricMapper::toEntity)
            .filter(metric::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(metricList).isNotNull();
        assertThat(metricList).hasSize(1);
        Metric testMetric = metricList.get(0);
        assertThat(testMetric.getMetric()).isEqualTo(DEFAULT_METRIC);
    }

    @Test
    void getAllMetrics() {
        // Initialize the database
        metric.setId(UUID.randomUUID());
        metricRepository.save(metric).block();

        // Get all the metricList
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
            .value(hasItem(metric.getId().toString()))
            .jsonPath("$.[*].metric")
            .value(hasItem(DEFAULT_METRIC));
    }

    @Test
    void getMetric() {
        // Initialize the database
        metric.setId(UUID.randomUUID());
        metricRepository.save(metric).block();

        // Get the metric
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, metric.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(metric.getId().toString()))
            .jsonPath("$.metric")
            .value(is(DEFAULT_METRIC));
    }

    @Test
    void getNonExistingMetric() {
        // Get the metric
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMetric() throws Exception {
        // Initialize the database
        metric.setId(UUID.randomUUID());
        metricRepository.save(metric).block();

        int databaseSizeBeforeUpdate = metricRepository.findAll().collectList().block().size();

        // Update the metric
        Metric updatedMetric = metricRepository.findById(metric.getId()).block();
        updatedMetric.metric(UPDATED_METRIC);
        MetricDTO metricDTO = metricMapper.toDto(updatedMetric);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, metricDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(metricDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Metric in the database
        List<Metric> metricList = metricRepository.findAll().collectList().block();
        assertThat(metricList).hasSize(databaseSizeBeforeUpdate);
        Metric testMetric = metricList.get(metricList.size() - 1);
        assertThat(testMetric.getMetric()).isEqualTo(UPDATED_METRIC);
    }

    @Test
    void putNonExistingMetric() throws Exception {
        int databaseSizeBeforeUpdate = metricRepository.findAll().collectList().block().size();
        metric.setId(UUID.randomUUID());

        // Create the Metric
        MetricDTO metricDTO = metricMapper.toDto(metric);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, metricDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(metricDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Metric in the database
        List<Metric> metricList = metricRepository.findAll().collectList().block();
        assertThat(metricList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMetric() throws Exception {
        int databaseSizeBeforeUpdate = metricRepository.findAll().collectList().block().size();
        metric.setId(UUID.randomUUID());

        // Create the Metric
        MetricDTO metricDTO = metricMapper.toDto(metric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(metricDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Metric in the database
        List<Metric> metricList = metricRepository.findAll().collectList().block();
        assertThat(metricList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMetric() throws Exception {
        int databaseSizeBeforeUpdate = metricRepository.findAll().collectList().block().size();
        metric.setId(UUID.randomUUID());

        // Create the Metric
        MetricDTO metricDTO = metricMapper.toDto(metric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(metricDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Metric in the database
        List<Metric> metricList = metricRepository.findAll().collectList().block();
        assertThat(metricList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMetricWithPatch() throws Exception {
        // Initialize the database
        metric.setId(UUID.randomUUID());
        metricRepository.save(metric).block();

        int databaseSizeBeforeUpdate = metricRepository.findAll().collectList().block().size();

        // Update the metric using partial update
        Metric partialUpdatedMetric = new Metric();
        partialUpdatedMetric.setId(metric.getId());

        partialUpdatedMetric.metric(UPDATED_METRIC);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMetric.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMetric))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Metric in the database
        List<Metric> metricList = metricRepository.findAll().collectList().block();
        assertThat(metricList).hasSize(databaseSizeBeforeUpdate);
        Metric testMetric = metricList.get(metricList.size() - 1);
        assertThat(testMetric.getMetric()).isEqualTo(UPDATED_METRIC);
    }

    @Test
    void fullUpdateMetricWithPatch() throws Exception {
        // Initialize the database
        metric.setId(UUID.randomUUID());
        metricRepository.save(metric).block();

        int databaseSizeBeforeUpdate = metricRepository.findAll().collectList().block().size();

        // Update the metric using partial update
        Metric partialUpdatedMetric = new Metric();
        partialUpdatedMetric.setId(metric.getId());

        partialUpdatedMetric.metric(UPDATED_METRIC);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMetric.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMetric))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Metric in the database
        List<Metric> metricList = metricRepository.findAll().collectList().block();
        assertThat(metricList).hasSize(databaseSizeBeforeUpdate);
        Metric testMetric = metricList.get(metricList.size() - 1);
        assertThat(testMetric.getMetric()).isEqualTo(UPDATED_METRIC);
    }

    @Test
    void patchNonExistingMetric() throws Exception {
        int databaseSizeBeforeUpdate = metricRepository.findAll().collectList().block().size();
        metric.setId(UUID.randomUUID());

        // Create the Metric
        MetricDTO metricDTO = metricMapper.toDto(metric);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, metricDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(metricDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Metric in the database
        List<Metric> metricList = metricRepository.findAll().collectList().block();
        assertThat(metricList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMetric() throws Exception {
        int databaseSizeBeforeUpdate = metricRepository.findAll().collectList().block().size();
        metric.setId(UUID.randomUUID());

        // Create the Metric
        MetricDTO metricDTO = metricMapper.toDto(metric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(metricDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Metric in the database
        List<Metric> metricList = metricRepository.findAll().collectList().block();
        assertThat(metricList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMetric() throws Exception {
        int databaseSizeBeforeUpdate = metricRepository.findAll().collectList().block().size();
        metric.setId(UUID.randomUUID());

        // Create the Metric
        MetricDTO metricDTO = metricMapper.toDto(metric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(metricDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Metric in the database
        List<Metric> metricList = metricRepository.findAll().collectList().block();
        assertThat(metricList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMetric() {
        // Initialize the database
        metric.setId(UUID.randomUUID());
        metricRepository.save(metric).block();

        int databaseSizeBeforeDelete = metricRepository.findAll().collectList().block().size();

        // Delete the metric
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, metric.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Metric> metricList = metricRepository.findAll().collectList().block();
        assertThat(metricList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
