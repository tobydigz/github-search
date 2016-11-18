package ng.gdg.devfestsw.githubsearch.services;

import java.util.List;

import ng.gdg.devfestsw.githubsearch.models.GithubError;
import ng.gdg.devfestsw.githubsearch.models.GithubRepository;

public interface GithubSearchService {

    void search(String query, Callback callback);

    void loadNextPage(Callback callback);

    void cancel();

    interface Callback {

        /**
         * Called when new repositories have been fetched from the service.
         */
        void onSuccess(List<GithubRepository> repositories);

        /**
         * Called when an error was returned from the service.
         */
        void onError(GithubError error);

        /**
         * We have a limit of 10 requests per minutes. So, this will be
         * called when we exceed that limit.
         */
        void onLimitExceeded();

        /**
         * Called when the user cancels a request.
         */
        void onCancelled();

        /**
         * Called when there is an error like a network failure, a parsing error
         * or something else.
         */
        void onFailure(Throwable t);
    }
}
