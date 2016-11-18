package ng.gdg.devfestsw.githubsearch.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ng.gdg.devfestsw.githubsearch.R;
import ng.gdg.devfestsw.githubsearch.models.GithubRepository;

public class GithubRepositoriesAdapter extends RecyclerView.Adapter<GithubRepositoriesAdapter.ViewHolder> {

    private List<GithubRepository> repositories;

    public GithubRepositoriesAdapter() {
        repositories = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.repository_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bind(repositories.get(position));
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    public void setRepositories(List<GithubRepository> repositories) {
        this.repositories = repositories;
        this.notifyDataSetChanged();
    }

    public void addRepositories(List<GithubRepository> repositories) {
        if (this.repositories == null) {
            this.repositories = new ArrayList<>();
        }

        this.repositories.addAll(repositories);

        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView urlTextView;

        ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.repository_name_text_view);
            urlTextView = (TextView) itemView.findViewById(R.id.repository_url_text_view);
        }

        void bind(GithubRepository repository) {
            nameTextView.setText(repository.getName());
            urlTextView.setText(repository.getURL());
        }
    }
}
