package si.uni_lj.fri.pbd.miniapp3.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.adapter.SpinnerAdapter;
import si.uni_lj.fri.pbd.miniapp3.database.entity.RecipeDetails;
import si.uni_lj.fri.pbd.miniapp3.models.Mapper;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeDetailsIM;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.IngredientsDTO;
import si.uni_lj.fri.pbd.miniapp3.models.dto.RecipesByIdDTO;
import si.uni_lj.fri.pbd.miniapp3.rest.RestAPI;
import si.uni_lj.fri.pbd.miniapp3.rest.ServiceGenerator;

public class DetailsActivity extends AppCompatActivity {

    private Long mealId;
    private String fragment;

    Bitmap bm;
    ImageView imageMeal;
    TextView strMeal;
    TextView strArea;
    TextView strIngredients ;
    TextView strMeasurements;
    TextView strInstructions;
    Button favoriteBtn;
    private RecipeDetailsIM recipeDetailsIM = new RecipeDetailsIM();
    private ViewModel viewModel;
    private List<RecipeDetails> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        viewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ViewModel.class);

        imageMeal = findViewById(R.id.detail_image);
        strMeal = findViewById(R.id.detail_strMeal);
        strArea = findViewById(R.id.detail_strArea);
        strIngredients = findViewById(R.id.detail_strIngredients);
        strMeasurements = findViewById(R.id.detail_strMeasurements);
        strInstructions = findViewById(R.id.detail_strInstructions);
        favoriteBtn = findViewById(R.id.add_favorite);

        Intent intent = getIntent();
        mealId = intent.getLongExtra("mealId", -1);
        fragment = intent.getStringExtra("fragment");

        System.out.println(mealId);
        System.out.println(fragment);

        if(mealId != -1)// proper mealId
        {
            if(fragment.equals("SEARCH"))
                callRecipeFromDTO();
            else if(fragment.equals("FAVORITE"))
                callRecipeFromDB();
        }

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(recipeDetailsIM.getFavorite());

                if(recipeDetailsIM.getFavorite() == false)
                {
                    viewModel.insertRecipe(list.get(0));
//                    Toast.makeText(getApplicationContext(), list.get(0).getStrMeal() + " is added to favorites", Toast.LENGTH_SHORT).show();
                    recipeDetailsIM.setFavorite(true);
                    favoriteBtn.setText("REMOVE FAVORITE");
                }
                else{
                    viewModel.deleteRecipe(mealId);
                    recipeDetailsIM.setFavorite(false);
                    if(fragment.equals("SEARCH"))
                        favoriteBtn.setText("ADD FAVORITE");
                    else
                        finish();
                }
            }
        });
    }

    private void callRecipeFromDB() {
        viewModel.getRecipesById(mealId);

        viewModel.getResult().observe(this,
                new Observer<List<RecipeDetails>>() {
                    @Override
                    public void onChanged(@Nullable final List<RecipeDetails> recipeDetails) {
                        if(recipeDetails.size()>0)
                        {
                            recipeDetailsIM = Mapper.mapRecipeDetailsToRecipeDetailsIm(true, recipeDetails.get(0));
                            printRecipeDetail(recipeDetailsIM);
                        }
                    }
                });
    }

    private void callRecipeFromDTO(){
        System.out.println("callRecipeFromDTO");

        RestAPI api;
        ServiceGenerator serviceGenerator = new ServiceGenerator();
        api = serviceGenerator.createService(RestAPI.class);
        Call<RecipesByIdDTO> call = api.getRecipesById(mealId);

        call.enqueue(new Callback<RecipesByIdDTO>() {
            @Override
            public void onResponse(Call<RecipesByIdDTO> call, Response<RecipesByIdDTO> response) {
                RecipesByIdDTO meals = response.body();
                list = meals.getRecipesById();
                recipeDetailsIM = new RecipeDetailsIM();
                recipeDetailsIM = Mapper.mapRecipeDetailsToRecipeDetailsIm(false, list.get(0));

                System.out.println(list.get(0).getStrMeal());

                if(list!=null&&list.size()>0)
                {
                    System.out.println(recipeDetailsIM);
                    printRecipeDetail(recipeDetailsIM);
                }

            }
            @Override
            public void onFailure(Call<RecipesByIdDTO> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "call recipe by id failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void printRecipeDetail(RecipeDetailsIM recipeDetailsIM)
    {
//        Thread mThread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    URL url = new URL(recipeDetailsIM.getStrMealThumb());
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setDoInput(true);
//                    conn.connect();
//
//                    InputStream is = conn.getInputStream();
//                    bm = BitmapFactory.decodeStream(is);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        mThread.start();
//
//        try{
//            mThread.join();
//            imageMeal.setImageBitmap(bm);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Glide.with(getApplicationContext()).load(recipeDetailsIM.getStrMealThumb()).override(800,400).into(imageMeal);

        strMeal.setText(recipeDetailsIM.getStrMeal());
        strArea.setText(recipeDetailsIM.getStrArea());
        String ingredient = "";
        if (recipeDetailsIM.getStrIngredient1() != null && !recipeDetailsIM.getStrIngredient1().equals(""))
            ingredient += (recipeDetailsIM.getStrIngredient1() + ", ");
        if (recipeDetailsIM.getStrIngredient2() != null && !recipeDetailsIM.getStrIngredient2().equals(""))
            ingredient += (recipeDetailsIM.getStrIngredient2() + ", ");
        if (recipeDetailsIM.getStrIngredient3() != null && !recipeDetailsIM.getStrIngredient3().equals(""))
            ingredient += (recipeDetailsIM.getStrIngredient3() + ", ");
        if (recipeDetailsIM.getStrIngredient4() != null &&!recipeDetailsIM.getStrIngredient4().equals(""))
            ingredient += (recipeDetailsIM.getStrIngredient4() + ", ");
        if (recipeDetailsIM.getStrIngredient5() != null &&!recipeDetailsIM.getStrIngredient5().equals(""))
            ingredient += (recipeDetailsIM.getStrIngredient5() + ", ");
        if (recipeDetailsIM.getStrIngredient6() != null &&!recipeDetailsIM.getStrIngredient6().equals(""))
            ingredient += (recipeDetailsIM.getStrIngredient6() + ", ");
        if (recipeDetailsIM.getStrIngredient7() != null &&!recipeDetailsIM.getStrIngredient7().equals(""))
            ingredient += (recipeDetailsIM.getStrIngredient7() + ", ");
        if (recipeDetailsIM.getStrIngredient8() != null &&!recipeDetailsIM.getStrIngredient8().equals(""))
            ingredient += (recipeDetailsIM.getStrIngredient8() + ", ");
        if (recipeDetailsIM.getStrIngredient9() != null &&!recipeDetailsIM.getStrIngredient9().equals(""))
            ingredient += (recipeDetailsIM.getStrIngredient9() + ", ");
        if (recipeDetailsIM.getStrIngredient10() != null &&!recipeDetailsIM.getStrIngredient10().equals(""))
            ingredient += (recipeDetailsIM.getStrIngredient10() + ", ");
        if (recipeDetailsIM.getStrIngredient11() != null &&!recipeDetailsIM.getStrIngredient11().equals(""))
            ingredient += (recipeDetailsIM.getStrIngredient11() + ", ");
        if (recipeDetailsIM.getStrIngredient12() != null &&!recipeDetailsIM.getStrIngredient12().equals(""))
            ingredient += (recipeDetailsIM.getStrIngredient12() + ", ");
        if (recipeDetailsIM.getStrIngredient13() != null &&!recipeDetailsIM.getStrIngredient13().equals(""))
            ingredient += (recipeDetailsIM.getStrIngredient13() + ", ");
        if (recipeDetailsIM.getStrIngredient14() != null &&!recipeDetailsIM.getStrIngredient14().equals(""))
            ingredient += (recipeDetailsIM.getStrIngredient14() + ", ");
        if (recipeDetailsIM.getStrIngredient15() != null &&!recipeDetailsIM.getStrIngredient15().equals(""))
            ingredient += (recipeDetailsIM.getStrIngredient15() + ", ");
        if (recipeDetailsIM.getStrIngredient16() != null &&!recipeDetailsIM.getStrIngredient16().equals(""))
            ingredient += (recipeDetailsIM.getStrIngredient16() + ", ");
        if (recipeDetailsIM.getStrIngredient17() != null &&!recipeDetailsIM.getStrIngredient17().equals(""))
            ingredient += (recipeDetailsIM.getStrIngredient17() + ", ");
        if (recipeDetailsIM.getStrIngredient18() != null &&!recipeDetailsIM.getStrIngredient18().equals(""))
            ingredient += (recipeDetailsIM.getStrIngredient18() + ", ");
        if (recipeDetailsIM.getStrIngredient19() != null &&!recipeDetailsIM.getStrIngredient19().equals(""))
            ingredient += (recipeDetailsIM.getStrIngredient19() + ", ");
        if (recipeDetailsIM.getStrIngredient20() != null &&!recipeDetailsIM.getStrIngredient20().equals(""))
            ingredient += recipeDetailsIM.getStrIngredient20();

        strIngredients.setText(ingredient);

        String measure = "";
        if (recipeDetailsIM.getStrMeasure1() != null && !recipeDetailsIM.getStrMeasure1().equals(""))
            measure += (recipeDetailsIM.getStrMeasure1() + ", ");
        if (recipeDetailsIM.getStrMeasure2() != null && !recipeDetailsIM.getStrMeasure2().equals(""))
            measure += (recipeDetailsIM.getStrMeasure2() + ", ");
        if (recipeDetailsIM.getStrMeasure3() != null && !recipeDetailsIM.getStrMeasure3().equals(""))
            measure += (recipeDetailsIM.getStrMeasure3() + ", ");
        if (recipeDetailsIM.getStrMeasure4() != null && !recipeDetailsIM.getStrMeasure4().equals(""))
            measure += (recipeDetailsIM.getStrMeasure4() + ", ");
        if (recipeDetailsIM.getStrMeasure5() != null && !recipeDetailsIM.getStrMeasure5().equals(""))
            measure += (recipeDetailsIM.getStrMeasure5() + ", ");
        if (recipeDetailsIM.getStrMeasure6() != null && !recipeDetailsIM.getStrMeasure6().equals(""))
            measure += (recipeDetailsIM.getStrMeasure6() + ", ");
        if (recipeDetailsIM.getStrMeasure7() != null && !recipeDetailsIM.getStrMeasure7().equals(""))
            measure += (recipeDetailsIM.getStrMeasure7() + ", ");
        if (recipeDetailsIM.getStrMeasure8() != null && !recipeDetailsIM.getStrMeasure8().equals(""))
            measure += (recipeDetailsIM.getStrMeasure8() + ", ");
        if (recipeDetailsIM.getStrMeasure9() != null && !recipeDetailsIM.getStrMeasure9().equals(""))
            measure += (recipeDetailsIM.getStrMeasure9() + ", ");
        if (recipeDetailsIM.getStrMeasure10() != null && !recipeDetailsIM.getStrMeasure10().equals(""))
            measure += (recipeDetailsIM.getStrMeasure10() + ", ");
        if (recipeDetailsIM.getStrMeasure11() != null && !recipeDetailsIM.getStrMeasure11().equals(""))
            measure += (recipeDetailsIM.getStrMeasure11() + ", ");
        if (recipeDetailsIM.getStrMeasure12() != null && !recipeDetailsIM.getStrMeasure12().equals(""))
            measure += (recipeDetailsIM.getStrMeasure12() + ", ");
        if (recipeDetailsIM.getStrMeasure13() != null && !recipeDetailsIM.getStrMeasure13().equals(""))
            measure += (recipeDetailsIM.getStrMeasure13() + ", ");
        if (recipeDetailsIM.getStrMeasure14() != null && !recipeDetailsIM.getStrMeasure14().equals(""))
            measure += (recipeDetailsIM.getStrMeasure14() + ", ");
        if (recipeDetailsIM.getStrMeasure15() != null && !recipeDetailsIM.getStrMeasure15().equals(""))
            measure += (recipeDetailsIM.getStrMeasure15() + ", ");
        if (recipeDetailsIM.getStrMeasure16() != null && !recipeDetailsIM.getStrMeasure16().equals(""))
            measure += (recipeDetailsIM.getStrMeasure16() + ", ");
        if (recipeDetailsIM.getStrMeasure17() != null && !recipeDetailsIM.getStrMeasure17().equals(""))
            measure += (recipeDetailsIM.getStrMeasure17() + ", ");
        if (recipeDetailsIM.getStrMeasure18() != null && !recipeDetailsIM.getStrMeasure18().equals(""))
            measure += (recipeDetailsIM.getStrMeasure18() + ", ");
        if (recipeDetailsIM.getStrMeasure19() != null && !recipeDetailsIM.getStrMeasure19().equals(""))
            measure += (recipeDetailsIM.getStrMeasure19() + ", ");
        if (recipeDetailsIM.getStrMeasure20() != null && !recipeDetailsIM.getStrMeasure20().equals(""))
            measure += (recipeDetailsIM.getStrMeasure20());

        strMeasurements.setText(measure);
        strInstructions.setText(recipeDetailsIM.getStrInstructions());
        if(recipeDetailsIM.getFavorite() == false)
        {
            favoriteBtn.setText("ADD FAVORITE");
        }
        else{
            favoriteBtn.setText("REMOVE FAVORITE");
        }
    }

}
