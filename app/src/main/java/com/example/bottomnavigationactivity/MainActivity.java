package com.example.bottomnavigationactivity;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bottomnavigationactivity.api.manager.ProductManage;
import com.example.bottomnavigationactivity.model.Product;
import com.example.bottomnavigationactivity.model.Record;
import com.example.bottomnavigationactivity.model.Result;
import com.example.bottomnavigationactivity.ui.BaseActivity;
import com.example.bottomnavigationactivity.ui.widget.PullToRefreshListView;
import com.example.bottomnavigationactivity.util.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MainActivity extends BaseActivity {

    private ProductManage mProductManage ;


    private Product mProduct ;
    private PullToRefreshListView mPullListView;
    private TAdapter mAdapter ;
    private static final String PRODUCT_URL = "/api/action/datastore_search?limit=40&resource_id=e395f96a-b194-4fec-8865-ec2bd6137370";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    MainActivity.this.setTitle("BottomNavigation");
                    return true;
                case R.id.navigation_dashboard:
                    MainActivity.this.setTitle("DashBoard");
                    return true;
                case R.id.navigation_notifications:
                    MainActivity.this.setTitle("Notification");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProductManage = new ProductManage(this);
        initUI();
        this.doConnection(Constant.TYPE_PRODUCT);
    }

    @Override
    public Result doFetchData(Object obj){
        //background thread fetch data
        Result result = (Result)obj;
        switch(result.getType()){
            case Constant.TYPE_PRODUCT:
                String url = PRODUCT_URL;
                result = mProductManage.getProductList(url);
                break;
        }
        return result;
    }

    @Override
    public void doProcessData(Object obj) throws Exception {
        //UI thread deal data
        Result result = (Result)obj;
        switch (result.getType()){
            case Constant.TYPE_PRODUCT:
                Product product = (Product) result.getData();
                mProduct = product;
                mAdapter.notifyDataSetChanged();
                mPullListView.onFooterRefreshComplete();
                break;
        }
    }

    @Override
    public void doErrorData(Object obj) throws Exception {
        Result result = (Result)obj;
        switch (result.getType()){
            case Constant.TYPE_PRODUCT:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        Toast.makeText(MainActivity.this,"click on ImageView :"+tag,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void initUI() {
        BottomNavigationView navView = findViewById(R.id.nav_view);

        mPullListView = findViewById(R.id.pullToRefresh);
        mAdapter = new TAdapter();
        mPullListView.setHeadRefreshEnable(false);
        mPullListView.setFootRefreshEnable(false);
        mPullListView.setAdapter(mAdapter);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private class ListViewItem{
        public TextView key;
        public TextView value;
        public ImageView icon ;
    }
    private class TAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return null==mProduct?0:caculateItemNum();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListViewItem listViewItem = null;

            if(null == convertView){
                convertView = getLayoutInflater().inflate(R.layout.listview_item,parent,false);
                listViewItem  = new ListViewItem();
                listViewItem.key = (TextView)convertView.findViewById(R.id.tv_key);
                listViewItem.value = (TextView)convertView.findViewById(R.id.tv_value);
                listViewItem.icon = (ImageView)convertView.findViewById(R.id.iv_icon);
                convertView.setTag(listViewItem);
            }else{
                listViewItem = (ListViewItem)convertView.getTag();
            }


            String year = getYearFromPosition(position);
            long number = sumOfQuartersInYear(year);


            boolean showImage = isQuarterDescrease(year);

            listViewItem.value.setText(number+"");
            if(showImage){
                listViewItem.icon.setVisibility(View.VISIBLE);
                listViewItem.icon.setOnClickListener(MainActivity.this);
                listViewItem.icon.setTag(year+"-decrease");
                listViewItem.key.setText(year+"-decrease");
            }else{
                listViewItem.icon.setVisibility(View.INVISIBLE);
//                listViewItem.icon.setOnClickListener(null);
                listViewItem.key.setText(year);
            }
            return convertView;
        }

        public int caculateItemNum(){
            if(null == mProduct) return 0;
            HashSet<String> set = new HashSet<String>();
            Record[] records = mProduct.getRecords();
            for(int i=0 ; i< records.length ; i++){
                Record record = records[i];
                set.add(record.getQuarter().substring(0,4));
            }
            return set.size();
        }

        public String getYearFromPosition(int position){
            HashSet<String> set = new HashSet<String>();
            ArrayList<String> list = new ArrayList<String>();
            int length = 0 ;
            Record[] records = mProduct.getRecords();
            for(int i=0 ; i< records.length ; i++) {
                Record record = records[i];
                length = set.size();
                String year = record.getQuarter().substring(0, 4);
                set.add(year);
                if (length != set.size()) {
                    list.add(year);
                }
            }
            return list.get(position);
        }

        public long sumOfQuartersInYear(String year){
            if(null == mProduct) return 0;
            long sum = 0 ;
            HashSet<String> set = new HashSet<String>();
            Record[] records = mProduct.getRecords();
            for(int i=0 ; i< records.length ; i++){
                Record record = records[i];
                if(year.equals(record.getQuarter().substring(0,4))){
                    sum += Long.valueOf(record.getVolume_of_sms());
                }
            }
            return sum;
        }

        /**
         * judge if it is a descreasing year
         * @param year
         * @return
         */
        public boolean isQuarterDescrease(String year){
            if(null == mProduct) return false;
            long[] quarters = new long[]{-1L,-1L,-1L,-1L};
            int tempLength = 0 ;
            int quarter = 0 ;
            Record[] records = mProduct.getRecords();
            for(int i=0 ; i< records.length ; i++){
                Record record = records[i];
                String quarterStr = record.getQuarter();
                if(year.equals(quarterStr.substring(0,4))){
                    tempLength = quarterStr.length();
                    quarter = Integer.parseInt(quarterStr.substring(tempLength-1, tempLength))-1;
                    quarters[quarter] = Long.valueOf(record.getVolume_of_sms());
                }
            }
            long lastQuater = -1L ;
            boolean isDescrease = true;
            for(int i=0 ; i< 4 ; i++){
                if(quarters[i] < 0) continue;
                if(lastQuater > 0 && lastQuater < quarters[i]){
                    isDescrease = false;
                    break;
                }
                lastQuater = quarters[i] ;
            }
            return isDescrease;
        }

    }
}
