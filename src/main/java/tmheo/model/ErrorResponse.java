package tmheo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by taemyung on 2016. 9. 11..
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse implements Serializable {

    private String error;

    @JsonProperty("error_description")
    private String errorDescription;

}
