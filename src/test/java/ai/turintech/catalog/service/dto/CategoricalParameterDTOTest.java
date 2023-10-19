package ai.turintech.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.turintech.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoricalParameterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoricalParameterDTO.class);
        CategoricalParameterDTO categoricalParameterDTO1 = new CategoricalParameterDTO();
        categoricalParameterDTO1.setId(1L);
        CategoricalParameterDTO categoricalParameterDTO2 = new CategoricalParameterDTO();
        assertThat(categoricalParameterDTO1).isNotEqualTo(categoricalParameterDTO2);
        categoricalParameterDTO2.setId(categoricalParameterDTO1.getId());
        assertThat(categoricalParameterDTO1).isEqualTo(categoricalParameterDTO2);
        categoricalParameterDTO2.setId(2L);
        assertThat(categoricalParameterDTO1).isNotEqualTo(categoricalParameterDTO2);
        categoricalParameterDTO1.setId(null);
        assertThat(categoricalParameterDTO1).isNotEqualTo(categoricalParameterDTO2);
    }
}
