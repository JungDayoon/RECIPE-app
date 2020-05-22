package si.uni_lj.fri.pbd.miniapp3.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.joda.time.MutableDateTime;

import java.util.List;

import si.uni_lj.fri.pbd.miniapp3.database.RecipeRepository;
import si.uni_lj.fri.pbd.miniapp3.database.dao.RecipeDao;
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;

public class ViewModel extends AndroidViewModel {

    private RecipeRepository repository;

    private MutableLiveData<List<RecipeDetails>> result;
    private LiveData<List<RecipeDetails>> allRecipes;

    public ViewModel(@NonNull Application application) {
        super(application);

        repository = new RecipeRepository(application);
        allRecipes = repository.getAllRecipes();
        result = repository.getResult();
    }

    public LiveData<List<RecipeDetails>> getAllRecipes(){
        return allRecipes;
    }

    public MutableLiveData<List<RecipeDetails>> getResult(){
        return result;
    }

    public void insertRecipe(RecipeDetails recipe)
    {
        repository.insertRecipe(recipe);
    }

    public void deleteRecipe(Long idMeal){
        repository.deleteRecipe(idMeal);
    }

    public void getRecipesById(Long idMeal){
        repository.getRecipesById(idMeal);
    }
}
