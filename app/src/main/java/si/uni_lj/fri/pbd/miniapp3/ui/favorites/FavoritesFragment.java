package si.uni_lj.fri.pbd.miniapp3.ui.favorites;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.adapter.RecyclerViewAdapter;
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;
import si.uni_lj.fri.pbd.miniapp3.ui.ViewModel;
import si.uni_lj.fri.pbd.miniapp3.ui.WrapContentGridLayoutManager;

public class FavoritesFragment extends Fragment {

    private ViewModel viewModel;
    private RecyclerViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel  = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(ViewModel.class);

        SwipeRefreshLayout mSwipeRefreshLayout = getActivity().findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //refresh
                observerSetup();
                recyclerSetup();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        observerSetup();
        recyclerSetup();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    private void observerSetup(){
        final Observer<List<RecipeDetails>> observer = new Observer<List<RecipeDetails>>() {
            @Override
            public void onChanged(List<RecipeDetails> recipeDetails) {
                adapter.setRecipeList(recipeDetails);
                adapter.notifyDataSetChanged();
            }
        };

        viewModel.getAllRecipes().observe(getViewLifecycleOwner(), observer);
    }

    private void recyclerSetup() {

        WrapContentGridLayoutManager layoutManager = new WrapContentGridLayoutManager(getContext(),2);
        RecyclerView recyclerView = getView().findViewById(R.id.favorite_recycler_view);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(null,"FAVORITE");
        recyclerView.setAdapter(adapter);
    }
}
