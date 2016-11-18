package ng.gdg.devfestsw.githubsearch.models;

public class GithubRepository {

    private long id;
    private String name;
    private String url;

    public GithubRepository(long id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    @Override
    public String toString() {
        return "GithubRepository(" + id + ", " + name + ", " + url + ")";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getURL() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
