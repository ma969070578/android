package com.aline.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.aline.activity.R;

/*
圆角ListView示例
设置list的间距及间距颜色
android:dividerHeight="20dip"
    android:divider="@color/gray"*/
 
public class CornerListView extends ListView {
    public CornerListView(Context context) {
        super(context);
    }

    public CornerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CornerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                int itemnum = pointToPosition(x, y);

                if (itemnum == AdapterView.INVALID_POSITION)
                        break;                 
                else{
                	if(itemnum==0){
                        if(itemnum==(getAdapter().getCount()-1)){                                    
                            setSelector(R.drawable.app_list_corner_round);
                        }else{
                            setSelector(R.drawable.app_list_corner_round_top);
                        }
	                }else if(itemnum==(getAdapter().getCount()-1))
	                        setSelector(R.drawable.app_list_corner_round_bottom);
	                else{                            
	                    setSelector(R.drawable.app_list_corner_shape);
	                }
                }

                break;
        case MotionEvent.ACTION_UP:
                break;
        }
        
        return super.onInterceptTouchEvent(ev);
    }
    
    
    
 // 设置listview高度
    public static void setListViewHeightBasedOnChildren(ListView listView) {
    	ListAdapter listAdapter = listView.getAdapter();
    	if (listAdapter == null) {
    		// pre-condition
    		return;
    	}
    	int totalHeight = 0;
    	for (int i = 0; i < listAdapter.getCount(); i++) {
    		View listItem = listAdapter.getView(i, null, listView);
    		listItem.measure(0, 0);
    		totalHeight += listItem.getMeasuredHeight();
    	}
    	ViewGroup.LayoutParams params = listView.getLayoutParams();
    	params.height = totalHeight
    			+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    	listView.setLayoutParams(params);
    }
}