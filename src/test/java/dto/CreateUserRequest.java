package dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data@AllArgsConstructor@NoArgsConstructor@Builder
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String title;
    private String gender;
    private String phone;
}
