package ir.welldone.appmanager.view.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class PagedResult<T> implements Serializable {

    private List<T> result;
    private Long totalRecords;

}
