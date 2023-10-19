package ai.turintech.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ModelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Model.class);
        Model model1 = new Model();
        model1.setId(UUID.randomUUID());
        Model model2 = new Model();
        model2.setId(model1.getId());
        assertThat(model1).isEqualTo(model2);
        model2.setId(UUID.randomUUID());
        assertThat(model1).isNotEqualTo(model2);
        model1.setId(null);
        assertThat(model1).isNotEqualTo(model2);
    }
}
