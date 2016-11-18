package ng.gdg.devfestsw.githubsearch.models;

public class GithubError {

    private String message;

    public GithubError(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "GithubError(" + message + ")";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
