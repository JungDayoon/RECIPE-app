package si.uni_lj.fri.pbd.miniapp3.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIdDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIngredientsDTO;

public interface RestAPI {

    @GET("list.php?i=list")
    Call<IngredientsDTO> getAllIngredients();

    @GET("filter.php")
    Call<RecipesByIngredientsDTO> getRecipesByIngredients(@Query("i") String ingredient);

    @GET("lookup.php")
    Call<RecipesByIdDTO> getRecipesById(@Query("i") Long recipe_id);

    // TODO: Add missing endpoints


}