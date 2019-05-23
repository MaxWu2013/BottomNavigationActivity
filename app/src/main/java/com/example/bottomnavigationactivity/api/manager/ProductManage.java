package com.example.bottomnavigationactivity.api.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.example.bottomnavigationactivity.api.Base;
import com.example.bottomnavigationactivity.model.Field;
import com.example.bottomnavigationactivity.model.Links;
import com.example.bottomnavigationactivity.model.Product;
import com.example.bottomnavigationactivity.model.Record;
import com.example.bottomnavigationactivity.model.Result;

import com.example.bottomnavigationactivity.util.AndroidUtils;
import com.example.bottomnavigationactivity.util.Commom;
import com.example.bottomnavigationactivity.util.Constant;
/**
 * 产品管理
 *
 */
public class ProductManage extends Base{
	
//	private static final String TAG = "ProductManage";

	public ProductManage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	/**
	 *

	 * @return List<Product> data
	 */
	public Result getProductList(String url){

//		Map<String, String> param = new HashMap<String, String>();
//		param.put("resource_id", resourceId);
//		param.put("limit",limit+"");


		String data =httpGet(url, null);

		boolean needToCache = true;
		if(null == data){
			// httprRequest Fail, get the latest cache
			needToCache = false;
			data = AndroidUtils.getSharedPreferencesString(context,url);
		}

		Result result = getResult(data,true);
		JSONObject jo = result.getJsonObject();
		Product product = new Product();
		if(result.isSucc()){
			try {

				//Cache the latest One url request
				if(needToCache){
					AndroidUtils.writeSharedPreferencesString(context,url ,data);
				}

				JSONObject resultObject = jo.getJSONObject("result");
				product.setResourceId(resultObject.optString("resource_id"));
				product.setLimit(resultObject.optInt("limit"));
				product.setTotal(resultObject.optInt("total"));

				Log.w("Product","product.resourceId:"+product.getResourceId());
				Log.w("Product","product.limit:"+product.getLimit());
				Log.w("Product","product.total:"+product.getTotal());

				JSONArray ja = resultObject.getJSONArray("fields");

				JSONObject temp ;
				Field[] fields = new Field[ja.length()];

				for (int i = 0; i < ja.length(); i++) {
					temp = ja.getJSONObject(i);
					Field field = new Field();
					field.setType(temp.optString("type"));
					field.setId(temp.optString("id"));

					Log.w("Product","field.type :"+field.getType() );
					Log.w("Product","field.id :"+field.getId() );

					fields[i] = field;
				}
				product.setFields(fields);

				ja = resultObject.getJSONArray("records");
				Record[] records = new Record[ja.length()];
				for(int i=0 ; i<ja.length(); i++){
					temp = ja.getJSONObject(i);
					Record record = new Record();
					record.setQuarter(temp.optString("quarter"));
					record.set_id(temp.optInt("_id"));
					record.setVolume_of_sms(temp.optString("volume_of_sms"));

					Log.w("Product","record.quarter :"+record.getQuarter() );
					Log.w("Product","record._id :"+record.get_id() );
					Log.w("Product","record.volume_of_sms :"+record.getVolume_of_sms() );

					records[i] = record;
				}
				product.setRecords(records);

				temp = resultObject.getJSONObject("_links");
				Links links = new Links();
				links.setStart(temp.optString("start"));
				links.setNext(temp.optString("next"));

				Log.w("Product","links.start :"+links.getStart() );
				Log.w("Product","links.next :"+links.getNext() );

				product.set_links(links);

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		result.setData(product);
		result.setJsonObject(null);
		return result;
	}

}
