# cordova-gaode-location V1.0.3
自定义使用高德SDK进行定位的插件，只支持android端，iOS 用浏览器定位就可以了，而且支持IOS的SDK较大，所以就没添加了；

## 1、高度地图版本介绍：
AMap_Location_V3.4.1_20170629.jar，请自己到高度地图API官网下载；
   
## 2、添加插件方式：
- 直接通过 url 安装：
```shell
cordova plugin add https://github.com/ww-gh/cordova-gaode-location.git --variable API_KEY="你在高度官网申请的API_KEY"
```
- 或通过 Cordova Plugins 安装，要求 Cordova CLI 5.0+：
```shell
cordova plugin add cordova-gaode-location --variable API_KEY="你在高度官网申请的API_KEY"
```
> 注意：使用前先到高度官网去申请自己的API Key；

## 3、使用说明：
- typescript语法
```typescript
cordova.plugins.GaoDeLocation.getCurrentPosition((resp: GaoDeposition) => {
                    console.log(resp);
                }, (err: ErrorInfo) => {
                    console.log(err);
                });

//GaoDeposition描述
class GaoDeposition {
    locationType: number;
    latitude: number;
    longitude: number;
    hasAccuracy: boolean;//是否有精确度
    accuracy: number;//精确度
    address: string;//地址
    province: string;//省份
    road: string;//街道
    speed: number;// 移速单位：米/秒
    bearing: number;// 角度
    satellites: number;// 星数
    time: number;// 时间
}

//ErrorInfo描述 
class ErrorInfo {
    errorCode: string;
    errorInfo: string;
}

 /**
  * 自定义的截取高德地图返回的错误描述
  * 返回的提醒信息格式是：
  * 问题说明
  * +空格
  * +请到http://lbs.amap.com/api/android-location-sdk/guide/utilities/errorcode/查看错误码说明,
  * +错误详细信息:问题排查策略
  * +#错误码
  * @param err
  * @return {string}
  */
 static getErrorInfo(err: ErrorInfo): string {
     let errInfo = '';
     if (err) {
         if (err.errorInfo.indexOf(' ') > 0) {//问题说明
             errInfo = err.errorInfo.substring(0, err.errorInfo.indexOf(' '));
         }
         if (err.errorInfo.indexOf('错误详细信息') > 0) {//问题排查策略
             errInfo = errInfo + err.errorInfo.substring(err.errorInfo.indexOf('错误详细信息'), err.errorInfo.length)
                     .replace("错误详细信息", "");
         } else {
             errInfo = err.errorInfo;
         }
     } else {
         errInfo = "定位出现未知错误，请稍后重试！"
     }
     return errInfo;
 }
```
- javascript语法
```javascript
cordova.plugins.GaoDeLocation.getCurrentPosition(function(resp){
                    console.log(resp);
                }, function(err){
                    console.log(err);
                });
```

## Support
- 邮箱：(wangwenxy@163.com)
- [故障反馈地址：](https://github.com/ww-gh/cordova-gaode-location/issues)
