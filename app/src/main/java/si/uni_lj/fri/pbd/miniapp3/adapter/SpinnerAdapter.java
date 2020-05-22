package si.uni_lj.fri.pbd.miniapp3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.ui.search.SearchFragment;

public class SpinnerAdapter extends BaseAdapter {
    String ingredient[];
    Context context;

    public SpinnerAdapter(String[] ingredient, Context context)
    {
        this.ingredient = ingredient;
        this.context = context;
    }
    @Override
    public int getCount() { ;
        return ingredient.length-1;
    }

    @Override
    public String getItem(int i) {
        return ingredient[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int i, @NonNull View convertView, ViewGroup viewGroup) {

        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, null);
        }

        TextView tv = convertView.findViewById(R.id.text_view_spinner_item);

        if(i == getCount()){
            tv.setText("");
            tv.setHint(getItem(getCount()));
        }

        tv.setText(ingredient[i]);

        //if textView clicked, make event
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, getItem(i) + "is selected", Toast.LENGTH_SHORT).show();
//            }
//        });

        return convertView;
    }
}
