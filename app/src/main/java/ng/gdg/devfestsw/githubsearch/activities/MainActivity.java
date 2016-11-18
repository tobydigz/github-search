package ng.gdg.devfestsw.githubsearch.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ng.gdg.devfestsw.githubsearch.R;
import ng.gdg.devfestsw.githubsearch.adapters.GithubRepositoriesAdapter;
import ng.gdg.devfestsw.githubsearch.databinding.ActivityMainBinding;
import ng.gdg.devfestsw.githubsearch.models.GithubError;
import ng.gdg.devfestsw.githubsearch.models.GithubRepository;
import ng.gdg.devfestsw.githubsearch.services.GithubSearchHttpService;
import ng.gdg.devfestsw.githubsearch.services.GithubSearchService;

public class MainActivity extends AppCompatActivity {

    private final static long THROTTLE_DELAY = 600;

    private ActivityMainBinding binding;

    private GithubSearchService service;
    private GithubSearchServiceCallback callback;
    private GithubRepositoriesAdapter adapter;

    private String searchQuery;
    private Timer throttler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        adapter = new GithubRepositoriesAdapter();
        service = new GithubSearchHttpService(this);
        callback = new GithubSearchServiceCallback();
        throttler = new Timer();

        binding.repositoriesView.setLayoutManager(new LinearLayoutManager(this));
        binding.repositoriesView.setAdapter(adapter);
        binding.repositoriesView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();

                if (lastItemPosition != RecyclerView.NO_POSITION && lastItemPosition == adapter.getItemCount() - 1) {
                    binding.loadNextPageProgressBar.setVisibility(View.VISIBLE);

                    callback.isLoadingNextPage = true;
                    callback.isSearching = false;
                    service.loadNextPage(callback);
                }
            }
        });

        binding.searchTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                service.cancel();
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable s) {
                throttleSearchRequest(s.toString());
            }
        });
    }

    private void throttleSearchRequest(String query) {
        this.searchQuery = query;

        cancelThrottledSearchRequests();

        binding.repositoryCountTextView.setText(R.string.searching);
        binding.searchProgressBar.setVisibility(View.VISIBLE);
        binding.repositoriesView.setVisibility(View.INVISIBLE);

        if (query.trim().isEmpty()) {
            service.cancel();
            adapter.setRepositories(new ArrayList<GithubRepository>());

            binding.repositoryCountTextView.setText(R.string.no_repository_found);
            binding.searchProgressBar.setVisibility(View.INVISIBLE);
            binding.repositoriesView.setVisibility(View.VISIBLE);
            return;
        }

        throttler.schedule(new TimerTask() {
            @Override
            public void run() {
                final String query = MainActivity.this.searchQuery;
                if (query.trim().isEmpty()) {
                    return;
                }

                callback.isSearching = true;
                callback.isLoadingNextPage = false;

                service.search(query, callback);
            }
        }, THROTTLE_DELAY);
    }

    private void cancelThrottledSearchRequests() {
        if (throttler != null) {
            throttler.cancel();
            throttler.purge();
            throttler = null;
            throttler = new Timer();
        }
    }

    class GithubSearchServiceCallback implements GithubSearchService.Callback {

        private boolean isSearching;
        private boolean isLoadingNextPage;

        @Override
        public void onSuccess(List<GithubRepository> repositories) {
            if (isSearching) {
                adapter.setRepositories(repositories);
            } else if (isLoadingNextPage) {
                adapter.addRepositories(repositories);
            }

            updateUI();
            Log.i("GithubSearchService", "Fetched " + repositories.size() + ": " + repositories);
        }

        @Override
        public void onError(GithubError error) {
            updateUI();
            Log.e("GithubSearchService", error.toString());
        }

        @Override
        public void onLimitExceeded() {
            updateUI();
            waitForRateLimitDelay();

            Log.e("GithubSearchService", "Rate Limit Exceeded.");
        }

        @Override
        public void onCancelled() {
            updateUI();
            Log.i("GithubSearchService", "Request cancelled");
        }

        @Override
        public void onFailure(Throwable t) {
            updateUI();
            Log.e("GithubSearchService", t.getMessage());
        }

        private void updateUI() {
            if (isSearching) {
                binding.searchProgressBar.setVisibility(View.INVISIBLE);
            } else if (isLoadingNextPage) {
                binding.loadNextPageProgressBar.setVisibility(View.GONE);
            }

            int count = adapter.getItemCount();
            binding.repositoryCountTextView.setText(count == 0 ? getResources().getString(R.string.no_repository_found)
                : getResources().getString(R.string.repositories_found, count, count > 1 ? "ies" : "y"));
            binding.repositoriesView.setVisibility(View.VISIBLE);
        }

        private void waitForRateLimitDelay() {
            binding.searchTextField.setEnabled(false);
            binding.repositoriesView.setLayoutFrozen(true);

            final Snackbar snackbar = Snackbar.make(binding.activityMain, getResources().getString(R.string.rate_limit_exceeded, 60, "s"), Snackbar.LENGTH_INDEFINITE);
            snackbar.show();

            final Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                int seconds = 60;

                @Override
                public void run() {
                    seconds--;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (seconds <= 0) {
                                snackbar.dismiss();
                                timer.cancel();

                                binding.searchTextField.setEnabled(true);
                                binding.repositoriesView.setLayoutFrozen(false);

                                return;
                            }

                            snackbar.setText(getResources().getString(R.string.rate_limit_exceeded, seconds, seconds > 1 ? "s" : ""));
                        }
                    });
                }
            }, 1000, 1000);
        }
    }
}
