package tmheo.exception;

/**
 * Created by taemyung on 2016. 9. 11..
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String id) {
        super("could not find resource '" + id + "'");
    }

}
