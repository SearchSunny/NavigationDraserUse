package com.example.navigationdrawer;

import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
/**
 * 
 * 描述：Navigation Drawer导航抽屉模式(需要v4包支持)
 */
public class MainActivity extends Activity implements OnItemClickListener{

	private String[] mPlanetTitles;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 mTitle = mDrawerTitle = getTitle();
	     mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
 
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
      //处理菜单点击事件
        mDrawerList.setOnItemClickListener(this);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        mDrawerToggle = new ActionBarDrawerToggle(this, 
        		mDrawerLayout, 
        		R.drawable.ic_drawer,
        		R.string.drawer_open, 
        		R.string.drawer_close){
        	
        	@Override
        	public void onDrawerOpened(View drawerView) {
        		getActionBar().setTitle(mTitle);
        		invalidateOptionsMenu();
        	}
        	
        	@Override
        	public void onDrawerClosed(View drawerView) {
        		getActionBar().setTitle(mDrawerTitle);
        		invalidateOptionsMenu();
        	}
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
        
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

		selectItem(position);
		
	}
	
	//设置fragment为当前view
	private void selectItem(int position) {
		 Fragment fragment = new PlanetFragment();
	        Bundle args = new Bundle();
	        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
	        fragment.setArguments(args);

	        
	        FragmentManager fragmentManager = getFragmentManager();
	        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

	        mDrawerList.setItemChecked(position, true);
	        setTitle(mPlanetTitles[position]);
	        mDrawerLayout.closeDrawer(mDrawerList);
	}
	
	public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = getResources().getStringArray(R.array.planets_array)[i];

            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
                            "drawable", getActivity().getPackageName());
            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
            getActivity().setTitle(planet);
            return rootView;
        }
    }
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
	    return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			
			return true;
		}
        switch(item.getItemId()) {
        case R.id.action_websearch:
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	
}
