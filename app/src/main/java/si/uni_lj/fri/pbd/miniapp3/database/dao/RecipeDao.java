package si.uni_lj.fri.pbd.miniapp3.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM RecipeDetails WHERE idMeal = :idMeal")
    List<RecipeDetails> getRecipesById(Long idMeal);

    @Insert
    void insertRecipe(RecipeDetails recipe);

    @Query("DELETE FROM RecipeDetails WHERE idMeal = :idMeal")
    void deleteRecipe(Long idMeal);

    @Query("SELECT * FROM RecipeDetails")
    LiveData<List<RecipeDetails>> getAllRecipes();
}
