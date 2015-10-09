package com.jieyangjiancai.zwj.common;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.bluetooth.BluetoothClass.Device;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.util.Log;


public class sysUtils {
	/**
	 * 判断当前app 是否前台.
	 * 
	 * @return
	 */
	public static boolean checkAppIsForeground(Context context) {
		boolean isForeground = false;

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		String servicePackName = context.getPackageName();
		// The first in the list of RunningTasks is always the foreground task.
		RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
		String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
		PackageManager pm = context.getPackageManager();
		PackageInfo foregroundAppPackageInfo = null;
		try {
			foregroundAppPackageInfo = pm.getPackageInfo(foregroundTaskPackageName, 0);
			String foregroundTaskAppName = foregroundAppPackageInfo.applicationInfo.loadLabel(pm).toString();
			Log.d("KMService", foregroundTaskAppName + foregroundAppPackageInfo.packageName + "  service " + servicePackName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (foregroundAppPackageInfo != null && foregroundAppPackageInfo.packageName.equals(servicePackName)) {
			isForeground = true;
		}
		return isForeground;
	}
	
	  /**
     * Get IP address from first non-localhost interface
     * @param ipv4  true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr); 
                        if (useIPv4) {
                            if (isIPv4) 
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }
    /**
     * 返回版本号
     * @param context
     * @return
     */
    public static String getVersionName(Context context){
    	PackageInfo pInfo;
    	String version = "";
		try {
			pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = pInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return version;
    }
    /**
     * {@link Device} id
     * @param context
     * @return
     */
    public static String getDeviceId(Context context){
    	TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    	String device_id = telephonyManager.getDeviceId();
    	return device_id;
    }
    
    
    
}
