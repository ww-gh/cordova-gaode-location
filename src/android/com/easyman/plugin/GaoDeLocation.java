package com.easyman.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;

import java.util.Locale;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;

/**
 * This class echoes a string called from JavaScript.
 */
public class GaoDeLocation extends CordovaPlugin {

   	private static final String ACTION_GETLOCATION = "getLocation";

   	private AMapLocationClient locationClient = null;
   	private AMapLocationClientOption locationOption = null;

   	private CallbackContext callbackContext = null;
   	private KITLocation kitLocation = new KITLocation();
   	private Context context;

   	@Override
   	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
   		context = this.cordova.getActivity().getApplicationContext();
   		super.initialize(cordova, webView);
   	}

   	@Override
   	public void onDestroy() {
   	    //销毁定位客户端，同时销毁本地定位服务。
   	    if(locationClient != null){
   	        locationClient.onDestroy();
   	    }
   		super.onDestroy();
   	}

   	@Override
   	public boolean execute(String action, JSONArray args,
   			CallbackContext callbackContext) throws JSONException {
   		this.callbackContext = callbackContext;
   		if (ACTION_GETLOCATION.equals(action)) {//.toLowerCase(Locale.CHINA)
   			kitLocation.startSingleLocation(context);
   			PluginResult r = new PluginResult(PluginResult.Status.NO_RESULT);
   			r.setKeepCallback(true);
   			callbackContext.sendPluginResult(r);
   			return true;
   		}
   		return false;
   	}

	private class KITLocation implements AMapLocationListener {
   		@Override
   		public void onLocationChanged(AMapLocation amapLocation) {
			if(amapLocation != null ){
				if (amapLocation.getErrorCode() == 0) {
					// 获取位置信息
					int locationType = amapLocation.getLocationType();
					Double latitude = amapLocation.getLatitude();
					Double longitude = amapLocation.getLongitude();
					boolean hasAccuracy = amapLocation.hasAccuracy();
					float accuracy = amapLocation.getAccuracy();
					String address = amapLocation.getAddress();
					String province = amapLocation.getProvince();
					String road = amapLocation.getRoad();
					// 速度
					float speed = amapLocation.getSpeed();
					// 角度
					float bearing = amapLocation.getBearing();
					// 星数
					int satellites = amapLocation.getExtras().getInt("satellites",0);
					// 时间
					long time = amapLocation.getTime();

					JSONObject jo = new JSONObject();
					try {
					    jo.put("locationType", locationType);
						jo.put("latitude", latitude);
						jo.put("longitude", longitude);
						jo.put("hasAccuracy", hasAccuracy);
						jo.put("accuracy", accuracy);
						jo.put("address", address);
						jo.put("province", province);
						jo.put("road", road);
						jo.put("speed", speed);
						jo.put("bearing", bearing);
						jo.put("satellites", satellites);
						jo.put("time", time);
					} catch (JSONException e) {
						jo = null;
						e.printStackTrace();
					}
					callbackContext.success(jo);
				} else {
				    JSONObject errJson = new JSONObject();
                    try {
					    errJson.put("errorInfo", amapLocation.getErrorInfo());
                        errJson.put("errorCode", amapLocation.getErrorCode());
					} catch (JSONException e) {
						errJson = null;
						e.printStackTrace();
					}
					callbackContext.error(errJson);
				}
			}else{
			    JSONObject errJson = new JSONObject();
                try {
                    errJson.put("errorInfo", "定位出现未知错误，请稍后重试！");
                    errJson.put("errorCode", "-1");
                } catch (JSONException e) {
                    errJson = null;
                    e.printStackTrace();
                }
				callbackContext.error(errJson);
			}
   		}

		void startSingleLocation(Context context) {
   			locationClient = new AMapLocationClient(context);
   			locationOption = new AMapLocationClientOption();
			/**
			 * 低功耗   Battery_Saving
			 * 高精度   Hight_Accuracy
			 * GPS     Device_Sensors
			 */
   			// 设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
			// 高精度定位模式：会同时使用网络定位和GPS定位，优先返回最高精度的定位结果，以及对应的地址描述信息。
			// 低功耗定位模式：不会使用GPS和其他传感器，只会使用网络定位（Wi-Fi和基站定位）；
			// 仅用设备定位模式：不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位，自 v2.9.0 版本支持返回地址描述信息。
   			locationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			// 获取最近3s内精度最高的一次定位结果：
			// 设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
			// 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。

			// 获取一次定位结果：该方法默认为false。
   			locationOption.setOnceLocation(true);
			// 设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
//			locationOption.setInterval(2000);
			// 设置是否返回地址信息（默认返回地址信息）
			locationOption.setNeedAddress(true);
			// 设置是否强制刷新WIFI，默认为强制刷新。每次定位主动刷新WIFI模块会提升WIFI定位精度，但相应的会多付出一些电量消耗。
//			locationOption.setWifiActiveScan(true);
			// 设置是否允许模拟软件Mock位置结果，多为模拟GPS定位结果，默认为false，不允许模拟位置。
//			locationOption.setMockEnable(false);
			// 设置定位请求超时时间，默认30000毫秒，建议超时时间不要低于8000毫秒。
			// 注意：自 V3.1.0 版本之后setHttpTimeOut(long httpTimeOut)方法不仅会限制低功耗定位、
			// 高精度定位两种模式的定位超时时间，同样会作用在仅设备定位时。
			// 如果单次定位发生超时情况，定位随即终止；连续定位状态下当前这一次定位会返回超时，但按照既定周期的定位请求会继续发起。
			locationOption.setHttpTimeOut(60000);
			// 设置是否开启定位缓存机制
			// 缓存机制默认开启，可以通过以下接口进行关闭。
			// 当开启定位缓存功能，在高精度模式和低功耗模式下进行的网络定位结果均会生成本地缓存，不区分单次定位还是连续定位。GPS定位结果不会被缓存。
			locationOption.setLocationCacheEnable(false);

			// 设置定位监听
			locationClient.setLocationListener(this);
   			// 设置定位参数
   			locationClient.setLocationOption(locationOption);
   			// 启动定位
   			locationClient.startLocation();
   		}
   	}
}
