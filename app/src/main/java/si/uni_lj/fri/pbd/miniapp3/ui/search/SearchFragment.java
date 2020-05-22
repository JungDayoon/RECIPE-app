package si.uni_lj.fri.pbd.miniapp3.ui.search;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.adapter.RecyclerViewAdapter;
import si.uni_lj.fri.pbd.miniapp3.adapter.SpinnerAdapter;
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIngredientsDTO;
import si.uni_lj.fri.pbd.miniapp3.rest.RestAPI;
import si.uni_lj.fri.pbd.miniapp3.rest.ServiceGenerator;
import si.uni_lj.fri.pbd.miniapp3.ui.WrapContentGridLayoutManager;

public class SearchFragment extends Fragment {
    private Spinner spinner;
    private String selectedIngredient;
    String[] ingredientList;
    ArrayList<RecipeDetails> list;
    RecyclerViewAdapter r_adapter;
    Boolean flag = false;
    MaterialProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("on create");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("on activityCreated");
        SwipeRefreshLayout mSwipeRefreshLayout = getActivity().findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callRecipesByIngredient();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        progressBar = getActivity().findViewById(R.id.materialProgressBar);

        WrapContentGridLayoutManager layoutManager = new WrapContentGridLayoutManager(getContext(),2);
        RecyclerView recyclerView = getView().findViewById(R.id.search_recycler_view);
        recyclerView.setLayoutManager(layoutManager);

        r_adapter = new RecyclerViewAdapter(list,"SEARCH");
//        r_adapter.setRecipeList(list, getContext());
        recyclerView.setAdapter(r_adapter);

        callIngredientsData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("on createView");
       return inflater.inflate(R.layout.fragment_search, container, false);
    }

    private void callIngredientsData(){
        RestAPI api;
        ServiceGenerator serviceGenerator = new ServiceGenerator();
        api = serviceGenerator.createService(RestAPI.class);
        Call<IngredientsDTO> call = api.getAllIngredients();

        System.out.println("call Ingredient by Data");

        call.enqueue(new Callback<IngredientsDTO>() {
            @Override
            public void onResponse(Call<IngredientsDTO> call, Response<IngredientsDTO> response) {
                IngredientsDTO ingredients = response.body();
                ArrayList<IngredientDTO> list = (ArrayList<IngredientDTO>) ingredients.getIngredients();

                if(list!=null&&list.size()>0)
                {
                    ingredientList = new String[list.size()+1];

                    for (int i = 0; i < list.size(); i++) {
                        System.out.println(list.get(i).getStrIngredient());
                        ingredientList[i] = list.get(i).getStrIngredient();
                    }

                    ingredientList[ingredientList.length-1] = "select ingredient"; //last item = hint
                    SpinnerAdapter adapter = new SpinnerAdapter(ingredientList, getActivity());
                    spinner = getActivity().findViewById(R.id.spinner);
                    spinner.setAdapter(adapter);
                    //spinner.setSelection(adapter.getCount());
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            selectedIngredient = adapter.getItem(i);
                            Toast.makeText(getActivity(), selectedIngredient + " is selected", Toast.LENGTH_SHORT).show();
                            callRecipesByIngredient();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
                else{

                }
            }
            @Override
            public void onFailure(Call<IngredientsDTO> call, Throwable t) {
                Toast.makeText(getActivity(), "Cannot connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callRecipesByIngredient(){
        RestAPI api;
        ServiceGenerator serviceGenerator = new ServiceGenerator();
        api = serviceGenerator.createService(RestAPI.class);
        Call<RecipesByIngredientsDTO> call = api.getRecipesByIngredients(selectedIngredient);

        progressBar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<RecipesByIngredientsDTO>() {
            @Override
            public void onResponse(Call<RecipesByIngredientsDTO> call, Response<RecipesByIngredientsDTO> response) {
                RecipesByIngredientsDTO meals = response.body();
                list = (ArrayList<RecipeDetails>) meals.getRecipesByIngredients();

                if(list.get(0) == null)
                    Toast.makeText(getActivity(), "Sorry, No recipes for this ingredient exist", Toast.LENGTH_SHORT);
                else
                {
                    r_adapter.setRecipeList(list);
                }

                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<RecipesByIngredientsDTO> call, Throwable t) {
                Toast.makeText(getActivity(), "Cannot connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

