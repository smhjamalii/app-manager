package ir.welldone.appmanager.view.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ir.welldone.appmanager.service.bvGroups.Delete;
import ir.welldone.appmanager.service.bvGroups.Edit;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class AppDTO implements Serializable {

    @NotNull(groups = {Edit.class, Delete.class})
    private Long id;

    @NotNull
    private String name;
    @NotNull
    private String desc;
    private String domain;
    @NotNull
    private Boolean enabled;
    private int version;

}
