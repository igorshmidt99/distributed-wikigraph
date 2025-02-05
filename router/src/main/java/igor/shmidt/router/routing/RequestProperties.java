package igor.shmidt.router.routing;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestProperties {

    private String reqId;

    private Integer pagesAmount;

    public static RequestProperties of(String reqId, Integer pagesAmount) {
        return new RequestProperties(reqId, pagesAmount);
    }

}
