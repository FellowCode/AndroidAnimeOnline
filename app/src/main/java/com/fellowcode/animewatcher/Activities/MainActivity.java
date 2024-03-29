package com.fellowcode.animewatcher.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fellowcode.animewatcher.Anime.AnimeList;
import com.fellowcode.animewatcher.Api.Api;
import com.fellowcode.animewatcher.R;
import com.fellowcode.animewatcher.User.UserRates;
import com.fellowcode.animewatcher.Utils.NavButtons;
import com.fellowcode.animewatcher.Adapters.MainPagerAdapter;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private MaterialSearchBar searchBar;

    boolean isSearch = false;

    public Api api;


    Toolbar toolbar;

    public ViewPager viewPager;
    TabLayout tabLayout;

    SearchView searchView;
    MenuItem searchItem;

    Context context = this;

    public ArrayList<AnimeList> animeLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Set<String> intentCategories = intent.getCategories();
        boolean startedFromLauncher = Intent.ACTION_MAIN.equals(intent.getAction()) && intentCategories != null && intentCategories.contains(Intent.CATEGORY_LAUNCHER);

        api = new Api(this);

        NavButtons navBtns = new NavButtons(this);
        navBtns.setOnClick(NavButtons.SEARCH, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchItem.isActionViewExpanded()) {
                    searchItem.expandActionView();
                    searchView.requestFocusFromTouch();
                } else {
                    searchView.clearFocus();
                }
            }
        });
        navBtns.select(NavButtons.HOME);
        navBtns.clearClick(NavButtons.HOME);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);


        // Получаем ViewPager и устанавливаем в него адаптер
        viewPager = findViewById(R.id.viewpager);
        // Передаём ViewPager в TabLayout
        tabLayout = findViewById(R.id.sliding_tabs);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(viewPager);

        checkDate();

        if (api.isShikiAuthenticated()) {
            if (startedFromLauncher || new UserRates(context).rates.size() == 0) {
                api.refreshShikiTokens(new Api.Auth() {
                    @Override
                    public void onSuccess() {
                        api.getUserRates(new UserRates.Response() {
                            @Override
                            public void onResponse() {
                                for (int i = 0; i < animeLists.size(); i++)
                                    animeLists.get(i).setUserRates(new UserRates(context));
                            }
                        });
                    }

                    @Override
                    public void onError(String response) {

                    }
                });
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Search(query);
                searchItem.collapseActionView();
                searchView.setQuery("", false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    searchItem.collapseActionView();
                    searchView.setQuery("", false);
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    void Search(String query) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("query", query);
        startActivity(intent);
        Log.d("test", "aasd");
    }

    void checkDate() {
        final int m = 11, y = 2019;
        Log.d("request", "checkDate");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://worldclockapi.com/api/json/est/now",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        try {
                            String currentDate = new JSONObject(response).getString("currentDateTime").split("T")[0];
                            int year = Integer.valueOf(currentDate.split("-")[0]);
                            int month = Integer.valueOf(currentDate.split("-")[1]);
                            int day = Integer.valueOf(currentDate.split("-")[2]);
                            if (year > y || month > m) {
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("req-error", error.toString());

                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
        /*api.Request("http://worldtimeapi.org/api/timezone/Europe/Moscow", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
            }
        });*/
    }

    @Override
    public void onResume(){
        super.onResume();
        for (int i=0;i<animeLists.size(); i++){
            animeLists.get(i).setUserRates(new UserRates(this));
        }
    }

}
