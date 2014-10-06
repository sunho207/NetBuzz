package edu.gatech.sunho207.netbuzz;

import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.ActionBar.Tab;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class Main extends FragmentActivity implements TabListener, OnItemClickListener {

    private DrawerLayout drawerLayout;
    private ListView listView;
    private String[] tabArray = {"Home", "Network", "Course", "My Buzz"};
    private ActionBarDrawerToggle drawerListener;
    private OnBackStackChangedListener drawerChangedListener;
    private NavAdapter navAdapter;
    private ViewPager viewPager = null;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Navigation Drawer
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        drawerChangedListener = new OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                setActionBarArrowDependingOnFragmentsBackStack();
            }
        };
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listView = (ListView) findViewById(R.id.drawer);
        navAdapter = new NavAdapter(this);
        listView.setAdapter(navAdapter);
        listView.setOnItemClickListener(this);
        drawerListener = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.drawer, R.string.open, R.string.close) {
            String saved;
            @Override
            public void onDrawerOpened(View drawerView) {
                drawerListener.setDrawerIndicatorEnabled(true);
                saved = getActionBar().getTitle().toString();
                getActionBar().setTitle("Services");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                setActionBarArrowDependingOnFragmentsBackStack();
                getActionBar().setTitle(saved);
            }
        };
        drawerLayout.setDrawerListener(drawerListener);
        getSupportFragmentManager().addOnBackStackChangedListener(drawerChangedListener);



        //ViewPager for sliding
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    actionBar.setSelectedNavigationItem(i);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));

        //Action Bar Tabs
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        Tab home = actionBar.newTab();
        home.setIcon(R.drawable.home);
        home.setTabListener(this);
        Tab nets = actionBar.newTab();
        nets.setIcon(R.drawable.network);
        nets.setTabListener(this);
        Tab classes = actionBar.newTab();
        classes.setIcon(R.drawable.group);
        classes.setTabListener(this);
        Tab buzz = actionBar.newTab();
        buzz.setIcon(R.drawable.mybuzz);
        buzz.setTabListener(this);
        actionBar.addTab(home);
        actionBar.addTab(nets);
        actionBar.addTab(classes);
        actionBar.addTab(buzz);
    }

 //Action Bar
    private void setActionBarArrowDependingOnFragmentsBackStack() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        drawerListener.setDrawerIndicatorEnabled(backStackEntryCount == 0);
    }

    @Override
    protected void onDestroy() {
        getSupportFragmentManager().removeOnBackStackChangedListener(drawerChangedListener);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);


        //SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerListener.isDrawerIndicatorEnabled() && drawerListener.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == android.R.id.home && getSupportFragmentManager().popBackStackImmediate()) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


//Tabs
    @Override
    public void onTabSelected(Tab tab, android.app.FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
        getActionBar().setTitle(tabArray[tab.getPosition()]);
    }

    @Override
    public void onTabUnselected(Tab tab, android.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(Tab tab, android.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerListener.onConfigurationChanged(newConfig);
    }

//Drawer
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        selectItem(i);
        drawerLayout.closeDrawers();
    }

    public void selectItem(int i) {
        listView.setItemChecked(i, true);
        Fragment newFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Intent intent = new Intent(this, Browser.class);
        switch (i) {
            case 0:
                intent.putExtra("title","BuzzPort");
                intent.putExtra("url","https://login.gatech.edu/cas/login?service=http%3A%2F%2Fbuzzport.gatech.edu%2Fsso%3Bjsessionid%3D3373F54694748C00E848CEE133ECC48E");
                startActivity(intent);
                break;
            case 1:
                intent.putExtra("title","T-Square");
                intent.putExtra("url","https://login.gatech.edu/cas/login?service=https%3A%2F%2Ft-square.gatech.edu%2Fsakai-login-tool%2Fcontainer");
                startActivity(intent);
                break;
            case 2:
                intent.putExtra("title","Mail");
                intent.putExtra("url","https://login.gatech.edu/cas/login?service=https%3A%2F%2Fmail.gatech.edu%2FGTpreauth%2F%3Flogin%3Dtrue");
                startActivity(intent);
                break;
            case 3:
                intent.putExtra("title","Calendar");
                intent.putExtra("url","http://www.registrar.gatech.edu/calendar/m/");
                startActivity(intent);
                break;
            case 4:
                intent.putExtra("title","Course Critique");
                intent.putExtra("url","https://critique.gatech.edu");
                startActivity(intent);
                break;
            case 5:
                intent.putExtra("title","BuzzCard");
                intent.putExtra("url","https://buzzcard.blackboard.com/webapps/portal/frameset.jsp");
                startActivity(intent);
                break;
            case 6:
                intent.putExtra("title","Dining Hours");
                intent.putExtra("url","http://mealplan.gatech.edu/pages/locations.aspx");
                startActivity(intent);
                break;
            case 7:
                intent.putExtra("title","Campus Map");
                intent.putExtra("url","http://maps.gatech.edu/d7/drupal/map");
                startActivity(intent);
                break;
            case 8:
                intent.putExtra("title","NextBus");
                intent.putExtra("url","http://nextbus.com/#_home");
                startActivity(intent);
                break;
            case 9:
                intent.putExtra("title","Stingerette");
                intent.putExtra("url","https://login.gatech.edu/cas/login?service=http%3A//ride.stingerette.com/student_ui/");
                startActivity(intent);
                break;
            case 10:
                break;
            case 11:
                ParseUser.logOut();
                finish();
                Intent login = new Intent(this, Login.class);
                startActivity(login);
                break;
            //newFragment = new CreateAssignment();
            //transaction.replace(R.id.frame_layout, newFragment);
            //transaction.addToBackStack(null);
            //transaction.commit();
        }

    }
}





//ViewPager Adapter
class MyAdapter extends FragmentPagerAdapter {

    public MyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        if (i == 0) {
            fragment = new Home();
        }
        if (i == 1) {
            fragment = new Nets();

        }
        if (i == 2) {
            fragment = new Classes();
        }
        if (i == 3) {
            fragment = new Buzz();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}

//Navigation Browser Adapter
class NavAdapter extends BaseAdapter {
    String[] drawerText;
    private Context context;
    int[] drawerImage = {R.drawable.buzzport, R.drawable.tsquare, R.drawable.mail, R.drawable.calendar,
            R.drawable.coursecritique, R.drawable.buzzcard, R.drawable.dining, R.drawable.map,
            R.drawable.nextbus, R.drawable.stingerette, R.drawable.settings, R.drawable.logout};

    public NavAdapter(Context context) {
        this.context = context;
        drawerText = context.getResources().getStringArray(R.array.drawer_array);
    }

    @Override
    public int getCount() {
        return drawerText.length;
    }

    @Override
    public Object getItem(int i) {
        return drawerText[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.custom_row, viewGroup, false);
        } else {
            row = view;
        }
        TextView textView = (TextView) row.findViewById(R.id.textView);
        ImageView imageView = (ImageView) row.findViewById(R.id.imageView);
        textView.setText(drawerText[i]);
        imageView.setImageResource(drawerImage[i]);

        return row;
    }
}