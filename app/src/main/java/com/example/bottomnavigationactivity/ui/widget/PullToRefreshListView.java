package com.example.bottomnavigationactivity.ui.widget;

import com.example.bottomnavigationactivity.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PullToRefreshListView extends ListView implements OnScrollListener {
	   
    private final static String TAG = "PullToRefreshListView";
    
    // 下拉刷新标志   
    private final static int PULL_To_REFRESH = 0;
    // 松开刷新标志   
    private final static int RELEASE_To_REFRESH = 1;
    // 正在刷新标志   
    private final static int REFRESHING = 2;
    // 刷新完成标志   
    private final static int DONE = 3;
  
    private LayoutInflater inflater;
  
    private LinearLayout headView;
    private TextView tipsTextview;
    private TextView lastUpdatedTextView;
    private ImageView arrowImageView;
    private ProgressBar progressBar;
    // 用来设置箭头图标动画效果   
    private RotateAnimation animation;
    private RotateAnimation reverseAnimation;
  
    // 用于保证startY的值在一个完整的touch事件中只被记录一次   
    private boolean isRecored;
    private long mLastRefreshTime = 0;
    
    
    private View mFootView;
    private View mFootViewProgress;
    private TextView mFootViewInfo;
    
    
  
//    private int headContentWidth;
    private int headContentHeight;
    private int headContentOriginalTopPadding;
  
    private int startY;
    private int firstItemIndex;
    private int currentScrollState;
  
    private int state;
  
    private boolean isBack;
    private boolean mHeadRefreshEnable = true;
    private boolean mFootRefreshEnable = true ;
    private boolean isNeedRefreshHeadView = true;
    private boolean isFootLoading = false ;  
    
    public OnRefreshListener refreshListener;
    
    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
  
    private void init(Context context) { 
    	//设置滑动效果
        animation = new RotateAnimation(0, -180,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(200);  
        animation.setFillAfter(true);
  
        reverseAnimation = new RotateAnimation(-180, 0,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,  
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(200);  
        reverseAnimation.setFillAfter(true);
        
        inflater = LayoutInflater.from(context);
        headView = (LinearLayout) inflater.inflate(R.layout.pull_to_refresh_head, null);
  
        arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
        arrowImageView.setMinimumWidth(50);
        arrowImageView.setMinimumHeight(50);
        progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
        tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
        lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);
        
        headContentOriginalTopPadding = headView.getPaddingTop();
        
        measureView(headView);
        headContentHeight = headView.getMeasuredHeight();
//      headContentWidth = headView.getMeasuredWidth();
        
        headView.setPadding(headView.getPaddingLeft(), -1 * headContentHeight, headView.getPaddingRight(), headView.getPaddingBottom());
        //headView.invalidate();

        //System.out.println("初始高度："+headContentHeight);
        //System.out.println("初始TopPad："+headContentOriginalTopPadding);
        
        addHeaderView(headView);  
        
        if(mFootView == null){
			LinearLayout footer = (LinearLayout) LayoutInflater.from(context).inflate(
					R.layout.pull_to_refresh_footer, this, false);
			mFootView = footer;
			mFootViewProgress = mFootView.findViewById(R.id.progress);
			mFootViewInfo = ((TextView)mFootView.findViewById(R.id.intro));
			mFootViewProgress.setVisibility(View.GONE);
			mFootViewInfo.setVisibility(View.GONE);
			mFootViewInfo.setText(R.string.pull_to_refresh_no_data);
		}
    	this.addFooterView(mFootView);
        
        //headView.setVisibility(View.GONE);
        currentScrollState = SCROLL_STATE_TOUCH_SCROLL;
        setOnScrollListener(this);
    }

  @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,  int totalItemCount) {
        firstItemIndex = firstVisibleItem;
        if(mHeadRefreshEnable){
        	isNeedRefreshHeadView = 0==firstItemIndex?true:false;
        }
//        ILog.d("PullToRefreshListView on Scroll ", "firstVisibleItem ="+firstVisibleItem);
//        ILog.d("PullToRefreshListView on Scroll ", "visibleItemCount ="+visibleItemCount);
//        ILog.d("PullToRefreshListView on Scroll ", "totalItemCount ="+totalItemCount);
        if(mFootRefreshEnable && !isFootLoading){
        	if(firstVisibleItem + visibleItemCount >= totalItemCount && currentScrollState != SCROLL_STATE_IDLE){
				mFootViewInfo.setText(R.string.loading_data);
				mFootViewInfo.setTextColor(getResources().getColor(R.color.text_color_step_3));
				mFootViewProgress.setVisibility(View.VISIBLE);
				mFootViewInfo.setVisibility(View.VISIBLE);
				if(refreshListener != null){
					refreshListener.onFootRefresh();
				}
				isFootLoading = true;
        	}
        }
    }
  @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    	currentScrollState = scrollState;
    }
  @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if(!isNeedRefreshHeadView){
    		return super.onTouchEvent(event);
    	}
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            if (!isRecored) {
                startY = (int) event.getY();
                isRecored = true;
                return true;
            }
            break;
        
        case MotionEvent.ACTION_CANCEL://失去焦点&取消动作
        case MotionEvent.ACTION_UP:
        	isRecored = false;
            isBack = false; 
            {
                if (state == PULL_To_REFRESH) {
                    state = DONE;
                    changeHeaderViewByState();                   
                    return true;
                    //System.out.println("当前-抬起-ACTION_UP：PULL_To_REFRESH-->DONE-由下拉刷新状态到刷新完成状态");
                }else if (state == RELEASE_To_REFRESH) {
                	state = REFRESHING;
                    changeHeaderViewByState();
                    onRefresh(); 
                    return true;
                } 
            }
            
            break;
  
        case MotionEvent.ACTION_MOVE:
            int tempY = (int) event.getY();
           
            //System.out.println("当前-滑动-ACTION_MOVE Y："+tempY);
            if (/*!isRecored && */firstItemIndex == 0) {
            	if(!isRecored){
            		 isRecored = true;
                     startY = tempY;
            	}
            	 int dentance = (tempY - startY);
            	 if(dentance <= 0){//超出边界
            		 //headView.setPadding(headView.getPaddingLeft(),-headContentHeight, headView.getPaddingRight(), headView.getPaddingBottom());   
                     //headView.invalidate();
            		 if(state != DONE){
            			 changeHeaderViewByState();
                		 isRecored = false;
                         state = DONE;
            		 }
            		 
            	     
            		
                    
            	 }else{        //未超出边界
            		 dentance=dentance>>1;
            		 headView.setPadding(headView.getPaddingLeft(),dentance-headContentHeight, headView.getPaddingRight(), headView.getPaddingBottom());   
                     headView.invalidate();
            		 switch(state){
                	 case RELEASE_To_REFRESH:
                		 if(dentance<headContentHeight){
                			 state = PULL_To_REFRESH;
                             changeHeaderViewByState();
                		 }
                		 return true;
                	 case PULL_To_REFRESH:
                		 if(dentance>headContentHeight){
                			 state = RELEASE_To_REFRESH;
                			 changeHeaderViewByState();
                		 }
                		 return true;
                	 case DONE:
                		 if(dentance>0){
                			 state = PULL_To_REFRESH;
                			 changeHeaderViewByState();
                			 return true;
                		 }
                		 break;
                	
                	 }
            		// return super.onTouchEvent(event);
            		
                     
            	 }
            }
            
            break;
        }
        return super.onTouchEvent(event);
    }
  
    // 头部刷新状态改变显示  
    private void changeHeaderViewByState() {
        switch (state) {
        case RELEASE_To_REFRESH:
        	
            arrowImageView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            tipsTextview.setVisibility(View.VISIBLE);
           // lastUpdatedTextView.setVisibility(View.VISIBLE);  
            setLastUpdateInfo(System.currentTimeMillis());
            
            arrowImageView.clearAnimation();  
            arrowImageView.startAnimation(animation);  
  
            tipsTextview.setText(R.string.pull_to_refresh_release_label);
  
            //Log.v(TAG, "当前状态，松开刷新");  
            break;
        case PULL_To_REFRESH:
        	
            progressBar.setVisibility(View.GONE);
            tipsTextview.setVisibility(View.VISIBLE);
            //lastUpdatedTextView.setVisibility(View.VISIBLE);  
            setLastUpdateInfo(System.currentTimeMillis());
            arrowImageView.clearAnimation();  
            arrowImageView.setVisibility(View.VISIBLE);  
            if (isBack) {  
                isBack = false;  
                arrowImageView.startAnimation(reverseAnimation);  
            } 
            tipsTextview.setText(R.string.pull_to_refresh_pull_label);

            //Log.v(TAG, "当前状态，下拉刷新");
            break;
  
        case REFRESHING: 
        	//System.out.println("刷新REFRESHING-TopPad："+headContentOriginalTopPadding);
        	headView.setPadding(headView.getPaddingLeft(), headContentOriginalTopPadding, headView.getPaddingRight(), headView.getPaddingBottom());
            headView.invalidate();
  
            progressBar.setVisibility(View.VISIBLE);
            arrowImageView.clearAnimation();
            arrowImageView.setVisibility(View.GONE);
            tipsTextview.setText(R.string.pull_to_refresh_refreshing_label);
            //lastUpdatedTextView.setVisibility(View.GONE);  
            setLastUpdateInfo(System.currentTimeMillis());
  
            //Log.v(TAG, "当前状态,正在刷新...");
            break;
        case DONE:
        	//System.out.println("完成DONE-TopPad："+(-1 * headContentHeight));
        	headView.setPadding(headView.getPaddingLeft(), -1 * headContentHeight, headView.getPaddingRight(), headView.getPaddingBottom());
            headView.invalidate();
  
            progressBar.setVisibility(View.GONE);
            arrowImageView.clearAnimation();
            // 此处更换图标   
            arrowImageView.setImageResource(R.mipmap.ic_pulltorefresh_arrow);
  
            tipsTextview.setText(R.string.pull_to_refresh_pull_label);
           // lastUpdatedTextView.setVisibility(View.VISIBLE);  
            setLastUpdateInfo(System.currentTimeMillis());
  
            //Log.v(TAG, "当前状态，done");
            break;
        }
    }
    /**
     * 设置头部刷新
     * @param enable 
     */
    public void setHeadRefreshEnable(boolean enable){
    	mHeadRefreshEnable = enable;
    	if(!enable){
    		isNeedRefreshHeadView = false;
    	}
    }
    /**
     * 获取头部刷新
     * @return
     */
    public boolean getHeadRefreshEnable(){
    	return mHeadRefreshEnable;
    }
    /**
     * 设置尾部刷新
     * @param enable
     */
    public void setFootRefreshEnable(boolean enable){
    	mFootRefreshEnable = enable ;
    	this.removeFooterView(mFootView);
    }
    public boolean getFootRefreshEnable(){
    	return mFootRefreshEnable ;
    }
    /*
    //点击刷新
    public void clickRefresh() {
    	setSelection(0);
    	state = REFRESHING;
        changeHeaderViewByState();
        onRefresh();
    }
    */
    public void setOnRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }
  
    public interface OnRefreshListener {
        public void onHeadRefresh();
        public void onFootRefresh();
    }
    //头部刷新结束
    public void onHeadRefreshComplete() {
        state = DONE;
        changeHeaderViewByState();
    }
    //头部刷新结束
    public void onHeadRefreshComplete(String update) {
        lastUpdatedTextView.setText(update);
        onHeadRefreshComplete();
    }
    //头部刷新结束
    public void onHeadRefreshComplete(long time){
    	
    	mLastRefreshTime = time;
    	onHeadRefreshComplete();
    }
    /**
     * 尾部刷新结束
     */
    public void onFooterRefreshComplete(){
    	mFootViewProgress.setVisibility(View.GONE);
    	mFootViewInfo.setVisibility(View.GONE);
		mFootViewInfo.setText(R.string.pull_to_refresh_no_data);
		mFootViewInfo.setTextColor(getResources().getColor(R.color.text_color_step_1));
		isFootLoading =false;
    }
    /**
     * 判断尾部是否在刷新
     * @return
     */
    public boolean IsFooterLoading(){
    	return isFootLoading;
    }
    public void setLastUpdateInfo(long time){
    	//lastUpdatedTextView.setText(str); 
    	if(mLastRefreshTime!=0){
    		long str = (time - mLastRefreshTime)/60*1000+1;
    		lastUpdatedTextView.setText("更新于" + str + "分钟前");
    	}
    }
    private void onRefresh() {
    	this.scrollTo(0, 0);
        if (refreshListener != null) {
            refreshListener.onHeadRefresh();
        }
    }
  
    // 计算headView的width及height值  
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,  
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,  
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,  
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }
    
    
	public ListView getRefreshableView() {
		// TODO Auto-generated method stub
		return this;
	}

	  
}
