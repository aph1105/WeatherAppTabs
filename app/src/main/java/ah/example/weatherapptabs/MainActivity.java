package ah.example.weatherapptabs;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TabHost;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;



public class MainActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {


    private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, MainActivity.TabInfo>();
    private SectionsPagerAdapter mPagerAdapter;
    private TabInfo mLastTab = null;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private class TabInfo {
        private String tag;
        private Class<?> clss;
        private Bundle args;
        private Fragment fragment;
        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag = tag;
            this.clss = clazz;
            this.args = args;
        }

    }


    class TabFactory implements TabHost.TabContentFactory {

        private final Context mContext;

        /**
         * @param context
         */
        public TabFactory(Context context) {
            mContext = context;
        }

        /** (non-Javadoc)
         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
         */
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }

    }
    /**
     * The {@link ViewPager} that will host the section contents.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getActionBar();
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, FragmentMain.class.getName()));
        fragments.add(Fragment.instantiate(this, FragmentDetail.class.getName()));
        fragments.add(Fragment.instantiate(this, FragmentForecast.class.getName()));
        this.mPagerAdapter  = new SectionsPagerAdapter(super.getSupportFragmentManager(), fragments);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
        this.initialiseTabHost(savedInstanceState);

        this.intialiseViewPager();



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.



        // Set up the ViewPager with the sections adapter.
    }

    public void onSaveInstanceState(Bundle outState) {

        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }


    private void intialiseViewPager() {


        this.mViewPager = (ViewPager)super.findViewById(R.id.pager);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setOnPageChangeListener(this);
    }

    private void initialiseTabHost(Bundle args) {
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo1 = new TabInfo(mPagerAdapter.getPageTitle(0).toString(), FragmentMain.class, args);
        TabInfo tabInfo2 = new TabInfo(mPagerAdapter.getPageTitle(1).toString(), FragmentForecast.class, args);
        TabInfo tabInfo3 = new TabInfo(mPagerAdapter.getPageTitle(2).toString(), FragmentDetail.class, args);

        MainActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec(mPagerAdapter.getPageTitle(0).toString()).setIndicator(mPagerAdapter.getPageTitle(0).toString()), tabInfo1);
        this.mapTabInfo.put(tabInfo1.tag, tabInfo1);
        MainActivity.AddTab(this, this. mTabHost, this.mTabHost.newTabSpec(mPagerAdapter.getPageTitle(1).toString()).setIndicator(mPagerAdapter.getPageTitle(1).toString()), tabInfo2 );
        this.mapTabInfo.put(tabInfo2.tag, tabInfo2);
        MainActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec(mPagerAdapter.getPageTitle(2).toString()).setIndicator(mPagerAdapter.getPageTitle(2).toString()), tabInfo3);
        this.mapTabInfo.put(tabInfo3.tag, tabInfo3);
        // Default to first tab
        //this.onTabChanged("Tab1");
        //
        mTabHost.setOnTabChangedListener(this);
    }

    private static void AddTab(MainActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        String tag = tabSpec.getTag();
        tabInfo.fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
        if(tabInfo.fragment != null && !tabInfo.fragment.isDetached()){
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.detach(tabInfo.fragment);
            ft.commit();
            activity.getSupportFragmentManager().executePendingTransactions();
        }
        tabHost.addTab(tabSpec);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.change_city) {
            showInputDialog();
        }
        return false;
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change city");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeCity(input.getText().toString());
            }
        });
        builder.show();
    }

    public void changeCity(String city){
        FragmentMain wf = (FragmentMain)getSupportFragmentManager()
                .findFragmentById(R.id.action_bar_container);
        wf.changeCity(city);
        new CityPreferences(this).setCity(city);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    /*
         * (non-Javadoc)
         *
         * @see
         * android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected
         * (int)
         */
    @Override
    public void onPageSelected(int position) {

        // Unfortunately when TabHost changes the current tab, it kindly
        // also takes care of putting focus on it when not in touch mode.
        // The jerk.
        // This hack tries to prevent this from pulling focus out of our
        // ViewPager.
       this.mTabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * (non-Javadoc)
     *
     * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
     */

    @Override
    public void onTabChanged(String tabId) {

       TabInfo newTab = this.mapTabInfo.get(tabId);
        if(mLastTab != newTab){
            FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
            if(mLastTab != null){
                if(mLastTab.fragment != null){
                    ft.detach(mLastTab.fragment);
                }
            }
         if(newTab != null){
             if(newTab.fragment == null){
                 newTab.fragment = Fragment.instantiate(this, newTab.clss.getName(),newTab.args);
                 ft.add(android.R.id.tabcontent,newTab.fragment,newTab.tag);

             }else{
                 ft.attach(newTab.fragment);
             }
         }
            mLastTab = newTab;
            ft.commit();
            this.getSupportFragmentManager().executePendingTransactions();
        }

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        private List<Fragment> fragments;
        @Override
        public Fragment getItem(int position) {

            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return this.fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
