package ng.gdg.devfestsw.githubsearch.services;

import java.util.List;

import ng.gdg.devfestsw.githubsearch.models.GithubError;
import ng.gdg.devfestsw.githubsearch.models.GithubRepository;

public interface GithubSearchService {

    void search(String query, Callback callback);

    void loadNextPage(Callback callback);

    void cancel();

    interface Callback {

        void onSuccess(List<GithubRepository> repositories);

        void onError(GithubError error);

        void onLimitExceeded();

        void onCancelled();

        void onFailure(Throwable t);
    }
}
