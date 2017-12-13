# cordova-gaode-location
自定义使用高德SDK进行定位的插件，只支持android端，iOS 用浏览器定位就可以了
1、高度地图版本是：
   AMap_Location_V3.4.1_20170629.jar
2、使用说明：
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
