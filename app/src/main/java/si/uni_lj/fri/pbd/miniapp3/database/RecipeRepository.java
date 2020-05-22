package si.uni_lj.fri.pbd.miniapp3.database;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import si.uni_lj.fri.pbd.miniapp3.database.dao.RecipeDao;
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;

public class RecipeRepository {

    private MutableLiveData<List<RecipeDetails>> result = new MutableLiveData<>();
    private LiveData<List<RecipeDetails>> allRecipes;

    private RecipeDao recipeDao;

    public RecipeRepository(Application application) {
        Database db;
        db = Database.getDatabase(application);
        recipeDao = db.RecipeDao();
        allRecipes = recipeDao.getAllRecipes();
    }

    public void insertRecipe(final RecipeDetails newRecipe)
    {
        Database.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                recipeDao.insertRecipe(newRecipe);
            }
        });
    }

    public void deleteRecipe(final Long idMeal)
    {
        Database.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                recipeDao.deleteRecipe(idMeal);
            }
        });
    }

    public void getRecipesById(final Long idMeal){
        Database.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                result.postValue(recipeDao.getRecipesById(idMeal));
            }
        });
    }

    public LiveData<List<RecipeDetails>> getAllRecipes(){
        return allRecipes;
    }
    public MutableLiveData<List<RecipeDetails>> getResult(){
        return result;
    }
}
