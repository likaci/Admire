package com.xiazhiri.admire;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiazhiri.admire.model.SideBar;
import com.xiazhiri.admire.model.WebContent;
import com.xiazhiri.admire.service.AdmireService;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MainActivity";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.list)
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        initDrawer();

        initWebsites("");

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(new WebsiteListAdapter());

    }

    private void initDrawer() {
        AdmireService.main()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1() {
                            @Override
                            public void call(Object o) {
                                List<SideBar> sideBarList = (List<SideBar>) o;
                                Menu menu = navigationView.getMenu();
                                SubMenu subMenu = null;
                                for (SideBar sideBar : sideBarList) {
                                    if ("label-1".equals(sideBar.getLevel())) {
                                        subMenu = menu.addSubMenu(sideBar.getTitle());
                                    } else if (subMenu != null && "label-2".equals(sideBar.getLevel())) {
                                        subMenu.add(sideBar.getTitle()).setIntent(new Intent(Intent.EXTRA_TEXT, Uri.parse(sideBar.getHref())));
                                    }
                                }
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                            }
                        }
                );
    }

    private void initWebsites(String url) {
        AdmireService.website(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1() {
                    @Override
                    public void call(Object o) {
                        ArrayList<WebContent> webContentList = (ArrayList<WebContent>) o;
                        WebsiteListAdapter adapter = (WebsiteListAdapter) list.getAdapter();
                        adapter.list = webContentList;
                        adapter.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        String url = item.getIntent().getDataString();
        initWebsites(url);
        return true;
    }

    public class WebsiteListAdapter extends RecyclerView.Adapter<WebsiteListAdapter.ViewHolder> {
        ArrayList<WebContent> list;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_card_website, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (list != null) {
                WebContent data = list.get(position);
                if (data != null) {
                    holder.title.setText(data.getTitle());
                    holder.desc.setText(data.getDesc());
                }
            }
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.img)
            ImageView img;
            @Bind(R.id.title)
            TextView title;
            @Bind(R.id.desc)
            TextView desc;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

    }

}
